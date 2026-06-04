package com.elyria.app.presentation.viewmodel

import com.elyria.app.core.base.UiText
import com.elyria.app.presentation.ui.screens.companion.CompanionUiMessage

data class CompanionUiState(
    val input: String = "",
    val messages: List<CompanionUiMessage> = emptyList(),
    val isSending: Boolean = false,
    val error: UiText? = null,
    val welcomePending: Boolean = true,
)
