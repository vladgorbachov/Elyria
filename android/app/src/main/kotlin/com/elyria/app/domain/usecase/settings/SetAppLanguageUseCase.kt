package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.repository.SettingsRepository
import javax.inject.Inject

class SetAppLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(language: AppLanguage): Result<Unit> {
        return runCatching {
            settingsRepository.setAppLanguage(language)
        }
    }
}
