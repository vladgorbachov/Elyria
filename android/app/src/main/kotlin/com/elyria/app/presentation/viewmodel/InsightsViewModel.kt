package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.core.utils.DateUtils
import com.elyria.app.domain.model.Insight
import com.elyria.app.domain.model.InsightPeriod
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodPatternSummary
import com.elyria.app.domain.model.MoodTrend
import com.elyria.app.domain.model.toDateRange
import com.elyria.app.domain.usecase.ai.GenerateCompanionResponseUseCase
import com.elyria.app.domain.usecase.insights.GetInsightsUseCase
import com.elyria.app.domain.usecase.insights.GetMoodPatternSummaryUseCase
import com.elyria.app.domain.usecase.mood.ObserveMoodEntriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DailyMoodPoint(
    val date: LocalDate,
    val averageScore: Float,
)

data class InsightsUiState(
    val period: InsightPeriod = InsightPeriod.WEEK,
    val insight: Insight? = null,
    val dailyScores: List<DailyMoodPoint> = emptyList(),
    val isLoading: Boolean = true,
    val aiSummary: String? = null,
    val isAiLoading: Boolean = false,
    val showReflectionError: Boolean = false,
    val patternSummary: MoodPatternSummary? = null,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val getInsightsUseCase: GetInsightsUseCase,
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
    private val generateCompanionResponseUseCase: GenerateCompanionResponseUseCase,
    private val getMoodPatternSummaryUseCase: GetMoodPatternSummaryUseCase,
) : BaseViewModel() {

    private val _period = MutableStateFlow(InsightPeriod.WEEK)
    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _period
                .flatMapLatest { period ->
                    combine(
                        getInsightsUseCase(period),
                        observeMoodEntriesUseCase(period.toDateRange()),
                    ) { insight, entries -> Triple(period, insight, entries) }
                }
                .collect { (period, insight, entries) ->
                    _uiState.update {
                        it.copy(
                            period = period,
                            insight = insight,
                            dailyScores = buildDailyScores(entries),
                            patternSummary = getMoodPatternSummaryUseCase(entries),
                            isLoading = false,
                            isAiLoading = true,
                            aiSummary = null,
                            showReflectionError = false,
                        )
                    }
                    loadReflection(insight, entries)
                }
        }
    }

    fun selectPeriod(period: InsightPeriod) {
        if (_period.value == period) return
        _uiState.update {
            it.copy(
                period = period,
                isLoading = true,
                isAiLoading = false,
                aiSummary = null,
                showReflectionError = false,
            )
        }
        _period.value = period
    }

    private suspend fun loadReflection(
        insight: Insight,
        entries: List<MoodEntry>,
    ) {
        generateCompanionResponseUseCase(insight, entries).fold(
            onSuccess = { summary ->
                _uiState.update {
                    it.copy(
                        aiSummary = summary,
                        isAiLoading = false,
                        showReflectionError = false,
                    )
                }
            },
            onFailure = {
                _uiState.update {
                    it.copy(
                        aiSummary = null,
                        isAiLoading = false,
                        showReflectionError = true,
                    )
                }
            },
        )
    }

    private fun buildDailyScores(
        entries: List<MoodEntry>,
    ): List<DailyMoodPoint> {
        return entries
            .groupBy { DateUtils.moodEntryToLocalDate(it.loggedAt) }
            .map { (date, dayEntries) ->
                DailyMoodPoint(
                    date = date,
                    averageScore = dayEntries.map { it.moodLevel.score }.average().toFloat(),
                )
            }
            .sortedBy { it.date }
    }

}

