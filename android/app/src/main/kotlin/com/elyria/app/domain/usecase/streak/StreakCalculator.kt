package com.elyria.app.domain.usecase.streak

import com.elyria.app.core.utils.DateUtils
import com.elyria.app.domain.model.StreakInfo
import java.time.LocalDate

/**
 * Pure streak calculation logic, extracted from UseCases for testing and reuse.
 */
object StreakCalculator {

    /**
     * Computes current and longest streak from log dates.
     *
     * Rules: one log per calendar day counts once; only today and past dates affect
     * current streak; a gap resets current streak.
     */
    fun calculate(
        entries: List<LocalDate>,
        today: LocalDate = DateUtils.today(),
    ): StreakInfo {
        if (entries.isEmpty()) {
            return StreakInfo(currentStreak = 0, longestStreak = 0, lastLogDate = null)
        }

        val uniqueDates = entries.distinct().sortedDescending()
        var currentStreak = 0
        var expectedDate = today
        var lastLogDate: LocalDate? = null

        for (date in uniqueDates) {
            if (date.isAfter(today)) continue

            if (date == expectedDate) {
                currentStreak++
                expectedDate = expectedDate.minusDays(1)
                lastLogDate = date
            } else if (date.isBefore(expectedDate)) {
                break
            }
        }

        val longestStreak = calculateLongestStreak(uniqueDates)

        return StreakInfo(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            lastLogDate = lastLogDate,
        )
    }

    private fun calculateLongestStreak(dates: List<LocalDate>): Int {
        if (dates.isEmpty()) return 0

        val sorted = dates.sorted()
        var maxStreak = 1
        var currentStreak = 1
        var prevDate = sorted[0]

        for (index in 1 until sorted.size) {
            val currentDate = sorted[index]
            if (currentDate == prevDate.plusDays(1)) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else {
                currentStreak = 1
            }
            prevDate = currentDate
        }

        return maxStreak
    }
}
