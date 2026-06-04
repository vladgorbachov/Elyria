package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.model.AppThemeMode
import com.elyria.app.domain.usecase.settings.ObserveAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class RootUiState(
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
    val showOnboarding: Boolean = true,
    val isLoading: Boolean = true,
)

@HiltViewModel
class RootViewModel @Inject constructor(
    observeAppSettingsUseCase: ObserveAppSettingsUseCase,
) : BaseViewModel() {

    val uiState: StateFlow<RootUiState> = observeAppSettingsUseCase()
        .map { settings ->
            RootUiState(
                themeMode = settings.themeMode,
                appLanguage = settings.appLanguage,
                showOnboarding = !settings.onboardingCompleted,
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RootUiState(),
        )
}
