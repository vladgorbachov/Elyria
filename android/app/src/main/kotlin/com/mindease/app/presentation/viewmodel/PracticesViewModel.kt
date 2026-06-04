package com.mindease.app.presentation.viewmodel

import com.mindease.app.core.base.BaseViewModel
import com.mindease.app.domain.model.Practice
import com.mindease.app.domain.usecase.practice.GetPracticesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PracticesUiState(
    val practices: List<Practice> = emptyList(),
)

@HiltViewModel
class PracticesViewModel @Inject constructor(
    getPracticesUseCase: GetPracticesUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(PracticesUiState(practices = getPracticesUseCase()))
    val uiState: StateFlow<PracticesUiState> = _uiState.asStateFlow()
}
