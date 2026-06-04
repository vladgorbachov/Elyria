package com.elyria.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.stringResource
import com.elyria.app.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elyria.app.presentation.ui.components.ElyriaScaffold
import com.elyria.app.presentation.ui.screens.companion.CompanionScreen
import com.elyria.app.presentation.ui.screens.home.HomeScreen
import com.elyria.app.presentation.ui.screens.insights.InsightsScreen
import com.elyria.app.presentation.ui.screens.mood.MoodLogScreen
import com.elyria.app.presentation.ui.screens.onboarding.OnboardingScreen
import com.elyria.app.presentation.ui.screens.practices.PracticeDetailScreen
import com.elyria.app.presentation.ui.screens.practices.PracticesScreen
import com.elyria.app.presentation.ui.screens.settings.SettingsScreen

@Composable
fun ElyriaNavGraph(
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
    val currentRoute = backStackEntry?.destination?.route.orEmpty()

    val currentScreen = when {
        currentRoute.contains("PracticeDetail") -> Screen.Practices
        currentRoute.contains("Home") -> Screen.Home
        currentRoute.contains("Mood") && !currentRoute.contains("MoodLog") -> Screen.Mood
        currentRoute.contains("Mood") -> Screen.Mood
        currentRoute.contains("Practices") -> Screen.Practices
        currentRoute.contains("Insights") -> Screen.Insights
        currentRoute.contains("Settings") -> Screen.Settings
        else -> Screen.Home
    }

    val showBottomBar = !currentRoute.contains("PracticeDetail") && !currentRoute.contains("CompanionChat")

    ElyriaScaffold(
        currentScreen = currentScreen,
        onNavigate = { screen ->
            navController.navigate(screen) {
                popUpTo(Screen.Home) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        showBottomBar = showBottomBar,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onLogMood = { navController.navigate(Screen.Mood) },
                    onOpenPractice = { id ->
                        navController.navigate(Screen.PracticeDetail(practiceId = id))
                    },
                    onOpenCompanion = { navController.navigate(Screen.CompanionChat()) },
                )
            }
            composable<Screen.Mood> {
                MoodLogScreen(
                    onSaved = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Home) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable<Screen.Practices> {
                PracticesScreen(
                    onPracticeClick = { id ->
                        navController.navigate(Screen.PracticeDetail(practiceId = id))
                    },
                )
            }
            composable<Screen.PracticeDetail> {
                PracticeDetailScreen(onBack = { navController.popBackStack() })
            }
            composable<Screen.Insights> {
                val companionPrefill = stringResource(R.string.insights_companion_prefill)
                InsightsScreen(
                    onOpenCompanion = {
                        navController.navigate(Screen.CompanionChat(prefill = companionPrefill))
                    },
                )
            }
            composable<Screen.CompanionChat> {
                CompanionScreen(onBack = { navController.popBackStack() })
            }
            composable<Screen.Settings> {
                SettingsScreen()
            }
        }
    }
}
