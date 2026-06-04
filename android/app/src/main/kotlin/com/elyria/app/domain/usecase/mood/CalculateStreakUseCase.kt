package com.elyria.app.domain.usecase.mood

import com.elyria.app.core.utils.DateUtils
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.StreakInfo
import com.elyria.app.domain.usecase.streak.StreakCalculator
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CalculateStreakUseCase @Inject constructor(
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
) {
    suspend operator fun invoke(): StreakInfo {
        val dates = observeMoodEntriesUseCase()
            .first()
            .map { DateUtils.moodEntryToLocalDate(it.loggedAt) }
        return StreakCalculator.calculate(dates)
    }

    fun calculateStreak(entries: List<MoodEntry>): StreakInfo {
        val dates = entries.map { DateUtils.moodEntryToLocalDate(it.loggedAt) }
        return StreakCalculator.calculate(dates)
    }
}
