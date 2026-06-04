package com.elyria.app.domain.usecase.onboarding

import com.elyria.app.domain.repository.SettingsRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        settingsRepository.setOnboardingCompleted(true)
    }
}
