package com.elyria.app.domain.repository

import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.model.AppThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<AppThemeMode>
    val onboardingCompleted: Flow<Boolean>
    val appLanguage: Flow<AppLanguage>

    suspend fun setThemeMode(mode: AppThemeMode)
    suspend fun setOnboardingCompleted(completed: Boolean)
    suspend fun setAppLanguage(language: AppLanguage)
}
