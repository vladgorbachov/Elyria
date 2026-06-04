package com.mindease.app.data.ai

interface AICompanion {
    suspend fun generateResponse(note: String, moodScore: Int?): String
}
