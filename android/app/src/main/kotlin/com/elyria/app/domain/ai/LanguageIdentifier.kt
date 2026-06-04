package com.elyria.app.domain.ai

import com.elyria.app.domain.model.CompanionLanguage

interface LanguageIdentifier {
    suspend fun identifyLanguage(text: String): CompanionLanguage
}
