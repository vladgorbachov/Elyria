package com.mindease.app.domain.usecase.settings

import com.mindease.app.domain.model.AppSettings
import com.mindease.app.domain.repository.SettingsRepository
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
        ) { themeMode, onboardingCompleted ->
            AppSettings(themeMode = themeMode, onboardingCompleted = onboardingCompleted)
        }
    }
}
