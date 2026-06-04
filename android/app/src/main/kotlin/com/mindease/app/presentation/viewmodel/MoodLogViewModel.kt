package com.mindease.app.presentation.viewmodel

import com.mindease.app.core.base.BaseViewModel
import com.mindease.app.domain.model.MoodLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface MoodLogUiState {
    data class Editing(
        val selectedMood: MoodLevel? = null,
        val note: String = "",
    ) : MoodLogUiState
}

@HiltViewModel
class MoodLogViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow<MoodLogUiState>(MoodLogUiState.Editing())
    val uiState: StateFlow<MoodLogUiState> = _uiState.asStateFlow()

    fun selectMood(level: MoodLevel) {
        val current = _uiState.value as? MoodLogUiState.Editing ?: return
        _uiState.update { MoodLogUiState.Editing(selectedMood = level, note = current.note) }
    }

    fun updateNote(note: String) {
        val current = _uiState.value as? MoodLogUiState.Editing ?: return
        _uiState.update { MoodLogUiState.Editing(selectedMood = current.selectedMood, note = note) }
    }
}
