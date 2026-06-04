package com.mindease.app.presentation.viewmodel

import com.mindease.app.core.base.BaseViewModel
import com.mindease.app.domain.model.StreakInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomeUiState(
    val streak: StreakInfo = StreakInfo(),
    val greeting: String = "Welcome back",
)

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
