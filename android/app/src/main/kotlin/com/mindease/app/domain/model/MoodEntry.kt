package com.mindease.app.domain.model

import java.time.LocalDateTime

data class MoodEntry(
    val id: Long = 0L,
    val moodLevel: MoodLevel,
    val note: String? = null,
    val loggedAt: LocalDateTime,
    val sentimentScore: Float? = null,
)
