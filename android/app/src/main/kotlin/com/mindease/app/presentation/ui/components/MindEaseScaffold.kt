package com.mindease.app.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mindease.app.R
import com.mindease.app.presentation.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val labelRes: Int,
    val icon: @Composable () -> Unit,
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, R.string.nav_home) {
        Icon(Icons.Default.Home, contentDescription = null)
    },
    BottomNavItem(Screen.Mood, R.string.nav_mood) {
        Icon(Icons.Default.Mood, contentDescription = null)
    },
    BottomNavItem(Screen.Practices, R.string.nav_practices) {
        Icon(Icons.Default.Favorite, contentDescription = null)
    },
    BottomNavItem(Screen.Insights, R.string.nav_insights) {
        Icon(Icons.Default.BarChart, contentDescription = null)
    },
    BottomNavItem(Screen.Settings, R.string.nav_settings) {
        Icon(Icons.Default.Settings, contentDescription = null)
    },
)

@Composable
fun MindEaseScaffold(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentScreen == item.screen,
                        onClick = { onNavigate(item.screen) },
                        icon = item.icon,
                        label = { Text(stringResource(item.labelRes)) },
                    )
                }
            }
        },
        content = content,
    )
}
