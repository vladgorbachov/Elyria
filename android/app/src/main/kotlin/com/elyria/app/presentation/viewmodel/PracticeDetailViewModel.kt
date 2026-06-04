package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.elyria.app.presentation.navigation.Screen
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.domain.model.Practice
import com.elyria.app.domain.usecase.practice.GetPracticeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PracticeDetailUiState(
    val practice: Practice? = null,
    val secondsRemaining: Int = 0,
    val totalSeconds: Int = 0,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val isCompleted: Boolean = false,
)

@HiltViewModel
class PracticeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPracticeByIdUseCase: GetPracticeByIdUseCase,
) : BaseViewModel() {

    private val practiceId: String = savedStateHandle.toRoute<Screen.PracticeDetail>().practiceId
    private var tickJob: Job? = null

    private val _uiState = MutableStateFlow(PracticeDetailUiState())
    val uiState: StateFlow<PracticeDetailUiState> = _uiState.asStateFlow()

    init {
        val practice = getPracticeByIdUseCase(practiceId)
        if (practice != null) {
            val totalSeconds = practice.durationMinutes * 60
            _uiState.value = PracticeDetailUiState(
                practice = practice,
                secondsRemaining = totalSeconds,
                totalSeconds = totalSeconds,
            )
        }
    }

    fun startTimer() {
        if (_uiState.value.isFinished) return
        _uiState.update { it.copy(isRunning = true) }
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (_uiState.value.isRunning && _uiState.value.secondsRemaining > 0) {
                delay(1_000)
                _uiState.update { state ->
                    val next = (state.secondsRemaining - 1).coerceAtLeast(0)
                    state.copy(
                        secondsRemaining = next,
                        isFinished = next == 0,
                        isRunning = next > 0,
                    )
                }
            }
        }
    }

    fun pauseTimer() {
        tickJob?.cancel()
        _uiState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        tickJob?.cancel()
        val total = _uiState.value.totalSeconds
        _uiState.update {
            it.copy(
                secondsRemaining = total,
                isRunning = false,
                isFinished = false,
                isCompleted = false,
            )
        }
    }

    fun completePractice() {
        // MVP: completion tracked in memory only until step 6 persistence.
        _uiState.update { it.copy(isCompleted = true) }
    }
}
