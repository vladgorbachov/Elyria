package com.elyria.app.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.elyria.app.R
import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.core.base.NavigateBack
import com.elyria.app.core.base.ShowMessage
import com.elyria.app.core.base.UiEvent
import com.elyria.app.core.base.UiText
import com.elyria.app.core.exception.AppException
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.domain.usecase.mood.LogMoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MoodLogUiState {
    data class Editing(
        val selectedMood: MoodLevel? = null,
        val note: String = "",
        val selectedEmotions: Set<EmotionCategory> = emptySet(),
        val selectedTriggers: Set<MoodTrigger> = emptySet(),
    ) : MoodLogUiState

    data object Saving : MoodLogUiState

    data class Success(val entry: MoodEntry) : MoodLogUiState

    data class Error(
        val message: UiText,
        val previous: Editing,
    ) : MoodLogUiState
}

@HiltViewModel
class MoodLogViewModel @Inject constructor(
    private val logMoodUseCase: LogMoodUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<MoodLogUiState>(MoodLogUiState.Editing())
    val uiState: StateFlow<MoodLogUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    fun selectMood(level: MoodLevel) {
        updateEditing { it.copy(selectedMood = level) }
    }

    fun updateNote(note: String) {
        updateEditing { it.copy(note = note.take(MAX_NOTE_LENGTH)) }
    }

    fun toggleEmotion(emotion: EmotionCategory) {
        updateEditing { editing ->
            val updated = editing.selectedEmotions.toMutableSet()
            if (emotion in updated) {
                updated.remove(emotion)
            } else if (updated.size < MAX_TAGS) {
                updated.add(emotion)
            }
            editing.copy(selectedEmotions = updated)
        }
    }

    fun toggleTrigger(trigger: MoodTrigger) {
        updateEditing { editing ->
            val updated = editing.selectedTriggers.toMutableSet()
            if (trigger in updated) {
                updated.remove(trigger)
            } else if (updated.size < MAX_TAGS) {
                updated.add(trigger)
            }
            editing.copy(selectedTriggers = updated)
        }
    }

    fun logMood() {
        val editing = _uiState.value as? MoodLogUiState.Editing ?: return
        val mood = editing.selectedMood
        if (mood == null) {
            viewModelScope.launch {
                _events.emit(ShowMessage(UiText.StringResource(R.string.mood_select_required)))
            }
            return
        }

        viewModelScope.launch {
            _uiState.value = MoodLogUiState.Saving
            logMoodUseCase(
                moodLevel = mood,
                note = editing.note,
                emotions = editing.selectedEmotions.toList(),
                triggers = editing.selectedTriggers.toList(),
            )
                .onSuccess { saved ->
                    _uiState.value = MoodLogUiState.Success(saved)
                    _events.emit(ShowMessage(UiText.StringResource(R.string.mood_saved)))
                    _events.emit(NavigateBack)
                }
                .onFailure { error ->
                    val message = error.toUserMessage()
                    _uiState.value = MoodLogUiState.Error(
                        message = message,
                        previous = editing,
                    )
                    _events.emit(ShowMessage(message))
                }
        }
    }

    fun dismissError() {
        val error = _uiState.value as? MoodLogUiState.Error ?: return
        _uiState.value = error.previous
    }

    private fun updateEditing(transform: (MoodLogUiState.Editing) -> MoodLogUiState.Editing) {
        val editing = _uiState.value as? MoodLogUiState.Editing ?: return
        _uiState.update { transform(editing) }
    }

    private fun Throwable.toUserMessage(): UiText {
        return when (this) {
            is AppException.Database -> UiText.StringResource(R.string.mood_save_failed)
            is AppException.Validation -> UiText.DynamicString(message ?: "")
            else -> UiText.StringResource(R.string.mood_save_failed)
        }
    }

    private companion object {
        const val MAX_NOTE_LENGTH = 200
        const val MAX_TAGS = 3
    }
}
