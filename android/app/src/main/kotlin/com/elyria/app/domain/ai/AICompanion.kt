package com.elyria.app.domain.ai

import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionContext
import com.elyria.app.domain.model.CompanionLanguage

interface AICompanion {
    suspend fun generateReflection(context: CompanionContext): String

    suspend fun generateChatResponse(
        userMessage: String,
        context: CompanionChatContext,
        language: CompanionLanguage,
    ): String
}
