package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.AppThemeMode
import com.elyria.app.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(themeMode: AppThemeMode): Result<Unit> {
        return runCatching {
            settingsRepository.setThemeMode(themeMode)
        }
    }
}
