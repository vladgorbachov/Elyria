package com.mindease.app.core.base

/**
 * One-shot UI events (snackbar, navigation) consumed once by the UI layer.
 */
interface UiEvent

data class ShowMessage(val message: String) : UiEvent

data object NavigateBack : UiEvent
