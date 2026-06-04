package com.elyria.app.domain.model

data class AppSettings(
    val themeMode: AppThemeMode,
    val onboardingCompleted: Boolean,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
)
