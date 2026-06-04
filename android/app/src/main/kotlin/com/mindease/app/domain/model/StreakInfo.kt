package com.mindease.app.domain.model

import java.time.LocalDate

data class StreakInfo(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastLogDate: LocalDate? = null,
)
