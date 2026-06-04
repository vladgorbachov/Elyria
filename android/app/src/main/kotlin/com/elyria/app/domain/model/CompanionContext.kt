package com.elyria.app.domain.model

import java.time.LocalDate

/**
 * Privacy-safe aggregate context for on-device companion reflections (no raw notes).
 */
data class CompanionContext(
    val averageMoodScore: Double,
    val trend: MoodTrend,
    val mostFrequentMood: MoodLevel?,
    val entryCount: Int,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
)
