package com.elyria.app.domain.model

data class CompanionChatContext(
    val latestMood: MoodLevel?,
    val latestMoodNote: String?,
    val currentStreak: Int,
    val recentEntryCount: Int,
    val averageMoodScore: Double?,
    val trend: MoodTrend?,
    val topEmotions: List<EmotionCategory> = emptyList(),
    val topTriggers: List<MoodTrigger> = emptyList(),
)
