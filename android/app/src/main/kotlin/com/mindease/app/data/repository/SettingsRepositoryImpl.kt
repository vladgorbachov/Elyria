package com.mindease.app.data.repository

import com.mindease.app.data.local.datastore.UserPreferences
import com.mindease.app.domain.model.AppThemeMode
import com.mindease.app.domain.repository.SettingsRepository
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

    override suspend fun setThemeMode(mode: AppThemeMode) {
        userPreferences.setThemeMode(mode)
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        userPreferences.setOnboardingCompleted(completed)
    }
}
