package com.elyria.app.domain.model

import java.time.LocalDate

data class Insight(
    val summary: String,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val averageMoodScore: Float,
    val trend: MoodTrend,
    val mostFrequentMood: MoodLevel?,
)
