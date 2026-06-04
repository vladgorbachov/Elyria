package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.AppSettings
import com.elyria.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppSettings> {
        return combine(
            settingsRepository.themeMode,
            settingsRepository.onboardingCompleted,
            settingsRepository.appLanguage,
        ) { themeMode, onboardingCompleted, appLanguage ->
            AppSettings(
                themeMode = themeMode,
                onboardingCompleted = onboardingCompleted,
                appLanguage = appLanguage,
            )
        }
    }
}
