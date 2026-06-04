package com.elyria.app.core.base

/**
 * Generic UI state wrapper for screens that load async data.
 * Screen-specific states may use dedicated sealed types instead when clearer.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Ready<T>(val data: T) : UiState<T>
    data class Failed(val message: String) : UiState<Nothing>
}
