package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.elyria.app.R
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.core.base.UiText
import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionMessageRole
import com.elyria.app.domain.model.InsightPeriod
import com.elyria.app.domain.model.toDateRange
import com.elyria.app.domain.usecase.ai.GenerateCompanionChatResponseUseCase
import com.elyria.app.domain.usecase.insights.GetInsightsUseCase
import com.elyria.app.domain.usecase.insights.GetMoodPatternSummaryUseCase
import com.elyria.app.domain.usecase.mood.GetLatestMoodUseCase
import com.elyria.app.domain.usecase.mood.ObserveMoodEntriesUseCase
import com.elyria.app.domain.usecase.mood.ObserveStreakUseCase
import com.elyria.app.presentation.ui.screens.companion.CompanionUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val generateCompanionChatResponseUseCase: GenerateCompanionChatResponseUseCase,
    private val getLatestMoodUseCase: GetLatestMoodUseCase,
    private val observeStreakUseCase: ObserveStreakUseCase,
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
    private val getInsightsUseCase: GetInsightsUseCase,
    private val getMoodPatternSummaryUseCase: GetMoodPatternSummaryUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CompanionUiState())
    val uiState: StateFlow<CompanionUiState> = _uiState.asStateFlow()

    private var nextMessageId = 0L

    init {
        savedStateHandle.get<String>("prefill")
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?.let { prefill -> _uiState.update { it.copy(input = prefill) } }
    }

    fun showWelcomeMessage() {
        if (!_uiState.value.welcomePending) return
        appendAssistantMessage(UiText.StringResource(R.string.companion_welcome))
        _uiState.update { it.copy(welcomePending = false) }
    }

    fun updateInput(value: String) {
        _uiState.update { it.copy(input = value, error = null) }
    }

    fun sendMessage() {
        val text = _uiState.value.input.trim()
        if (text.isBlank()) {
            _uiState.update {
                it.copy(error = UiText.StringResource(R.string.companion_empty_message))
            }
            return
        }
        if (_uiState.value.isSending) return

        appendUserMessage(text)
        _uiState.update { it.copy(input = "", isSending = true, error = null) }

        viewModelScope.launch {
            val context = buildChatContext()
            generateCompanionChatResponseUseCase(text, context).fold(
                onSuccess = { response ->
                    appendAssistantMessage(UiText.DynamicString(response))
                    _uiState.update { it.copy(isSending = false) }
                },
                onFailure = {
                    appendAssistantMessage(UiText.StringResource(R.string.companion_response_fallback))
                    _uiState.update {
                        it.copy(
                            isSending = false,
                            error = UiText.StringResource(R.string.companion_error),
                        )
                    }
                },
            )
        }
    }

    private suspend fun buildChatContext(): CompanionChatContext {
        val latest = getLatestMoodUseCase().first()
        val streak = observeStreakUseCase().first()
        val weekEntries = observeMoodEntriesUseCase(InsightPeriod.WEEK.toDateRange()).first()
        val insight = getInsightsUseCase(InsightPeriod.WEEK).first()
        val patterns = getMoodPatternSummaryUseCase(weekEntries)

        return CompanionChatContext(
            latestMood = latest?.moodLevel,
            latestMoodNote = latest?.note?.trim()?.take(MAX_NOTE_CONTEXT_LENGTH)?.ifBlank { null },
            currentStreak = streak.currentStreak,
            recentEntryCount = weekEntries.size,
            averageMoodScore = if (weekEntries.isEmpty()) null else insight.averageMoodScore.toDouble(),
            trend = if (weekEntries.isEmpty()) null else insight.trend,
            topEmotions = patterns.topEmotions,
            topTriggers = patterns.topTriggers,
        )
    }

    private fun appendUserMessage(text: String) {
        _uiState.update { state ->
            state.copy(messages = state.messages + newMessage(CompanionMessageRole.USER, text))
        }
    }

    private fun appendAssistantMessage(text: UiText) {
        _uiState.update { state ->
            state.copy(messages = state.messages + newMessage(CompanionMessageRole.ASSISTANT, text))
        }
    }

    private fun newMessage(role: CompanionMessageRole, text: UiText): CompanionUiMessage {
        return CompanionUiMessage(
            id = nextMessageId++,
            role = role,
            text = text,
        )
    }

    private fun newMessage(role: CompanionMessageRole, text: String): CompanionUiMessage {
        return newMessage(role, UiText.DynamicString(text))
    }

    private companion object {
        const val MAX_NOTE_CONTEXT_LENGTH = 200
    }
}
