package com.elyria.app.presentation.ui.screens.companion

import com.elyria.app.core.base.UiText
import com.elyria.app.domain.model.CompanionMessageRole

data class CompanionUiMessage(
    val id: Long,
    val role: CompanionMessageRole,
    val text: UiText,
)
