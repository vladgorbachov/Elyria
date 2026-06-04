package com.mindease.app.domain.repository

import com.mindease.app.domain.model.AppThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<AppThemeMode>
    val onboardingCompleted: Flow<Boolean>

    suspend fun setThemeMode(mode: AppThemeMode)
    suspend fun setOnboardingCompleted(completed: Boolean)
}
