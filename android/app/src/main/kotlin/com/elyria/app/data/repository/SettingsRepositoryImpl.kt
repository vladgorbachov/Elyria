package com.elyria.app.data.repository

import com.elyria.app.data.local.datastore.UserPreferences
import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.model.AppThemeMode
import com.elyria.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences,
) : SettingsRepository {

    override val themeMode: Flow<AppThemeMode> = userPreferences.themeMode

    override val onboardingCompleted: Flow<Boolean> =
        userPreferences.onboardingCompleted

    override val appLanguage: Flow<AppLanguage> = userPreferences.appLanguage

    override suspend fun setThemeMode(mode: AppThemeMode) {
        userPreferences.setThemeMode(mode)
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        userPreferences.setOnboardingCompleted(completed)
    }

    override suspend fun setAppLanguage(language: AppLanguage) {
        userPreferences.setAppLanguage(language)
    }
}
