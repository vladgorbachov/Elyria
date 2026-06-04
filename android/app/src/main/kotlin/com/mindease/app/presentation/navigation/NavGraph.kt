package com.mindease.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mindease.app.presentation.ui.components.MindEaseScaffold
import com.mindease.app.presentation.ui.screens.home.HomeScreen
import com.mindease.app.presentation.ui.screens.insights.InsightsScreen
import com.mindease.app.presentation.ui.screens.mood.MoodLogScreen
import com.mindease.app.presentation.ui.screens.onboarding.OnboardingScreen
import com.mindease.app.presentation.ui.screens.practices.PracticesScreen
import com.mindease.app.presentation.ui.screens.settings.SettingsScreen

@Composable
fun MindEaseNavGraph(
    showOnboarding: Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val startDestination = if (showOnboarding) Screen.Onboarding else Screen.Main

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Screen.Onboarding> {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Screen.Main) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.Main> {
            MainTabNavGraph()
        }
    }
}

@Composable
private fun MainTabNavGraph() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.let { dest ->
        when {
            dest.hasRoute(Screen.Home::class) -> Screen.Home
            dest.hasRoute(Screen.Mood::class) -> Screen.Mood
            dest.hasRoute(Screen.Practices::class) -> Screen.Practices
            dest.hasRoute(Screen.Insights::class) -> Screen.Insights
            dest.hasRoute(Screen.Settings::class) -> Screen.Settings
            else -> Screen.Home
        }
    } ?: Screen.Home

    MindEaseScaffold(
        currentScreen = currentScreen,
        onNavigate = { screen ->
            navController.navigate(screen) {
                popUpTo(Screen.Home) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Screen.Home> { HomeScreen() }
            composable<Screen.Mood> { MoodLogScreen() }
            composable<Screen.Practices> { PracticesScreen() }
            composable<Screen.Insights> { InsightsScreen() }
            composable<Screen.Settings> { SettingsScreen() }
        }
    }
}
