package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.Practice
import com.elyria.app.domain.model.StreakInfo
import com.elyria.app.domain.model.WellbeingProgress
import com.elyria.app.domain.usecase.mood.GetLatestMoodUseCase
import com.elyria.app.domain.usecase.mood.ObserveStreakUseCase
import com.elyria.app.domain.usecase.practice.GetPracticesUseCase
import com.elyria.app.domain.usecase.wellbeing.GetWellbeingProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val streak: StreakInfo = StreakInfo(),
    val latestMood: MoodLevel? = null,
    val featuredPractice: Practice? = null,
    val wellbeingProgress: WellbeingProgress = WellbeingProgress(0, 0, 0, 0),
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeStreakUseCase: ObserveStreakUseCase,
    getLatestMoodUseCase: GetLatestMoodUseCase,
    getPracticesUseCase: GetPracticesUseCase,
    getWellbeingProgressUseCase: GetWellbeingProgressUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(featuredPractice = getPracticesUseCase().firstOrNull()),
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                observeStreakUseCase(),
                getLatestMoodUseCase(),
                getWellbeingProgressUseCase(),
            ) { streak, latest, progress ->
                val current = _uiState.value
                current.copy(
                    streak = streak,
                    latestMood = latest?.moodLevel,
                    wellbeingProgress = progress,
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }
}
