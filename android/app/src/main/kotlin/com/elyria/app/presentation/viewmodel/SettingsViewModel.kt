package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.core.base.LaunchExportDocument
import com.elyria.app.core.base.UiEvent
import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.model.AppThemeMode
import com.elyria.app.domain.usecase.settings.SetAppLanguageUseCase
import com.elyria.app.domain.usecase.settings.BuildExportDataUseCase
import com.elyria.app.domain.usecase.settings.DeleteAllUserDataUseCase
import com.elyria.app.domain.usecase.settings.NoExportDataException
import com.elyria.app.domain.usecase.settings.ObserveAppSettingsUseCase
import com.elyria.app.domain.usecase.settings.SetThemeModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class SettingsUiState(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
    val isExporting: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val message: SettingsMessage? = null,
)

sealed interface SettingsMessage {
    data object ExportSuccess : SettingsMessage
    data object ExportFailed : SettingsMessage
    data object ExportNoData : SettingsMessage
    data object DeleteSuccess : SettingsMessage
    data object DeleteFailed : SettingsMessage
    data object ThemeUpdateFailed : SettingsMessage
    data object LanguageUpdateFailed : SettingsMessage
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val setThemeModeUseCase: SetThemeModeUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase,
    private val buildExportDataUseCase: BuildExportDataUseCase,
    private val deleteAllUserDataUseCase: DeleteAllUserDataUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            observeAppSettingsUseCase().collect { settings ->
                _uiState.update {
                    it.copy(
                        themeMode = settings.themeMode,
                        appLanguage = settings.appLanguage,
                    )
                }
            }
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        if (_uiState.value.appLanguage == language) return
        viewModelScope.launch {
            setAppLanguageUseCase(language).onFailure {
                _uiState.update { it.copy(message = SettingsMessage.LanguageUpdateFailed) }
            }
        }
    }

    fun onThemeSelected(mode: AppThemeMode) {
        if (_uiState.value.themeMode == mode) return
        viewModelScope.launch {
            setThemeModeUseCase(mode).onFailure {
                _uiState.update { it.copy(message = SettingsMessage.ThemeUpdateFailed) }
            }
        }
    }

    fun prepareExport() {
        if (_uiState.value.isExporting || _uiState.value.isDeleting) return
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, message = null) }
            buildExportDataUseCase()
                .onSuccess { json ->
                    val fileName = "elyria-export-${LocalDate.now()}.json"
                    _events.emit(LaunchExportDocument(fileName = fileName, json = json))
                }
                .onFailure { error ->
                    val message = if (error is NoExportDataException) {
                        SettingsMessage.ExportNoData
                    } else {
                        SettingsMessage.ExportFailed
                    }
                    _uiState.update { it.copy(message = message) }
                }
            _uiState.update { it.copy(isExporting = false) }
        }
    }

    fun onExportWriteResult(success: Boolean) {
        _uiState.update {
            it.copy(
                message = if (success) SettingsMessage.ExportSuccess else SettingsMessage.ExportFailed,
            )
        }
    }

    fun requestDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }

    fun dismissDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }

    fun deleteAllData() {
        if (_uiState.value.isDeleting || _uiState.value.isExporting) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(isDeleting = true, showDeleteConfirmation = false, message = null)
            }
            deleteAllUserDataUseCase()
                .onSuccess {
                    _uiState.update {
                        it.copy(isDeleting = false, message = SettingsMessage.DeleteSuccess)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(isDeleting = false, message = SettingsMessage.DeleteFailed)
                    }
                }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}
