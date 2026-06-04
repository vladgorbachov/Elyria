package com.elyria.app.domain.usecase.ai

import com.elyria.app.domain.ai.LanguageIdentifier
import com.elyria.app.domain.model.CompanionLanguage
import javax.inject.Inject

class DetectMessageLanguageUseCase @Inject constructor(
    private val languageIdentifier: LanguageIdentifier,
) {
    suspend operator fun invoke(text: String): CompanionLanguage {
        val normalizedText = text.trim()
        if (normalizedText.length < MIN_LANGUAGE_DETECTION_LENGTH) {
            return CompanionLanguage.UNKNOWN
        }
        return try {
            languageIdentifier.identifyLanguage(normalizedText)
        } catch (_: Exception) {
            CompanionLanguage.UNKNOWN
        }
    }

    private companion object {
        const val MIN_LANGUAGE_DETECTION_LENGTH = 4
    }
}
