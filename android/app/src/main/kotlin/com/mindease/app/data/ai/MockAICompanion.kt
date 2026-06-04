package com.mindease.app.data.ai

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAICompanion @Inject constructor() : AICompanion {
    override suspend fun generateResponse(note: String, moodScore: Int?): String {
        if (note.isBlank()) {
            return "Thanks for checking in. How are you feeling right now?"
        }
        return "I hear you. Take a slow breath — you're doing enough for today."
    }
}
