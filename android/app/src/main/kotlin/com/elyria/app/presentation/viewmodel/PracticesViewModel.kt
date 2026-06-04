package com.elyria.app.presentation.viewmodel

import com.elyria.app.core.base.BaseViewModel
import com.elyria.app.domain.model.Practice
import com.elyria.app.domain.usecase.practice.GetPracticesUseCase
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
