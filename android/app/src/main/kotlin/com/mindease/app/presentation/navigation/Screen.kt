package com.mindease.app.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations for Navigation Compose.
 */
@Serializable
sealed interface Screen {
    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Main : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Mood : Screen

    @Serializable
    data object Practices : Screen

    @Serializable
    data object Insights : Screen

    @Serializable
    data object Settings : Screen
}
