package com.elyria.app.domain.model

data class MoodPatternSummary(
    val topEmotions: List<EmotionCategory>,
    val topTriggers: List<MoodTrigger>,
    val lowMoodTriggers: List<MoodTrigger>,
    val entryCount: Int,
)
