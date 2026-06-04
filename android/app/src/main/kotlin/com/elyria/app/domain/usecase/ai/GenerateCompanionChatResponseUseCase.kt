package com.elyria.app.domain.usecase.ai

import com.elyria.app.domain.ai.AICompanion
import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionLanguage
import javax.inject.Inject

class GenerateCompanionChatResponseUseCase @Inject constructor(
    private val aiCompanion: AICompanion,
    private val detectMessageLanguageUseCase: DetectMessageLanguageUseCase,
) {
    suspend operator fun invoke(
        userMessage: String,
        context: CompanionChatContext,
    ): Result<String> {
        val normalizedMessage = userMessage.trim()
        if (normalizedMessage.isBlank()) {
            return Result.success(BLANK_MESSAGE_PROMPT)
        }

        val safeMessage = normalizedMessage.take(MAX_MESSAGE_LENGTH)
        val language = resolveResponseLanguage(safeMessage)
        return runCatching {
            aiCompanion.generateChatResponse(
                userMessage = safeMessage,
                context = context,
                language = language,
            )
        }
    }

    private suspend fun resolveResponseLanguage(message: String): CompanionLanguage {
        return try {
            when (val detected = detectMessageLanguageUseCase(message)) {
                CompanionLanguage.UNKNOWN -> CompanionLanguage.ENGLISH
                else -> detected
            }
        } catch (_: Exception) {
            CompanionLanguage.ENGLISH
        }
    }

    private companion object {
        const val MAX_MESSAGE_LENGTH = 500
        const val BLANK_MESSAGE_PROMPT =
            "You can write a few words about how you feel, and Elyria will reflect gently with you."
    }
}
