package com.elyria.app.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.model.AppThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val themeMode: Flow<AppThemeMode> = dataStore.data.map { prefs ->
        when (prefs[KEY_THEME]) {
            THEME_LIGHT -> AppThemeMode.LIGHT
            THEME_DARK -> AppThemeMode.DARK
            else -> AppThemeMode.SYSTEM
        }
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_DONE] ?: false
    }

    val appLanguage: Flow<AppLanguage> = dataStore.data.map { prefs ->
        AppLanguage.fromCode(prefs[KEY_APP_LANGUAGE])
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        dataStore.edit { prefs ->
            prefs[KEY_THEME] = when (mode) {
                AppThemeMode.LIGHT -> THEME_LIGHT
                AppThemeMode.DARK -> THEME_DARK
                AppThemeMode.SYSTEM -> THEME_SYSTEM
            }
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_DONE] = completed
        }
    }

    suspend fun setAppLanguage(language: AppLanguage) {
        dataStore.edit { prefs ->
            prefs[KEY_APP_LANGUAGE] = language.code
        }
    }

    private companion object {
        val KEY_THEME = stringPreferencesKey("theme_mode")
        val KEY_ONBOARDING_DONE = booleanPreferencesKey("onboarding_completed")
        val KEY_APP_LANGUAGE = stringPreferencesKey("app_language")
        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }
}
