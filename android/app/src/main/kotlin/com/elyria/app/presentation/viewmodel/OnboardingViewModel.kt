package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.domain.usecase.onboarding.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface OnboardingUiState {
    data object Idle : OnboardingUiState
    data object Saving : OnboardingUiState
    data object Done : OnboardingUiState
    data class Error(val message: String) : OnboardingUiState
}

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun completeOnboarding() {
        if (_uiState.value is OnboardingUiState.Saving) return
        viewModelScope.launch {
            _uiState.update { OnboardingUiState.Saving }
            runCatching { completeOnboardingUseCase() }
                .onSuccess { _uiState.update { OnboardingUiState.Done } }
                .onFailure { e ->
                    _uiState.update {
                        OnboardingUiState.Error(e.message ?: "Unknown error")
                    }
                }
        }
    }
}
