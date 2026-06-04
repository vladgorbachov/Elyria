package com.mindease.app.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mindease.app.domain.model.AppThemeMode

private val LightColorScheme = lightColorScheme(
    primary = ColorPalette.PrimaryLight,
    onPrimary = Color.White,
    secondary = ColorPalette.AccentGrowth,
    background = ColorPalette.BackgroundLight,
    onBackground = ColorPalette.TextPrimaryLight,
    surface = ColorPalette.SurfaceLight,
    onSurface = ColorPalette.TextPrimaryLight,
    onSurfaceVariant = ColorPalette.TextSecondaryLight,
    tertiary = com.mindease.app.core.constants.MoodColors.lavender,
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorPalette.PrimaryDark,
    onPrimary = Color.White,
    secondary = ColorPalette.AccentGrowth,
    background = ColorPalette.BackgroundDark,
    onBackground = ColorPalette.TextPrimaryDark,
    surface = ColorPalette.SurfaceDark,
    onSurface = ColorPalette.TextPrimaryDark,
    onSurfaceVariant = ColorPalette.TextSecondaryDark,
    tertiary = com.mindease.app.core.constants.MoodColors.lavender,
)

@Composable
fun MindEaseTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = mindEaseTypography(),
        content = content,
    )
}
