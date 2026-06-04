package com.elyria.app.core.base

/**
 * One-shot UI events (snackbar, navigation) consumed once by the UI layer.
 */
interface UiEvent

data class ShowMessage(val message: UiText) : UiEvent

// UiText lives in this package (UiText.kt)

data object NavigateBack : UiEvent
