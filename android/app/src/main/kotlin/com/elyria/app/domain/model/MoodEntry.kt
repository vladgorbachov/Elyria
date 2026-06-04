package com.elyria.app.domain.model

import java.time.LocalDateTime

data class MoodEntry(
    val id: Long = 0L,
    val moodLevel: MoodLevel,
    val note: String? = null,
    val loggedAt: LocalDateTime,
    val sentimentScore: Float? = null,
    val emotions: List<EmotionCategory> = emptyList(),
    val triggers: List<MoodTrigger> = emptyList(),
)
