package com.elyria.app.domain.usecase.streak

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class StreakCalculatorTest {

    @Test
    fun emptyList_returnsZeroStreaks() {
        val result = StreakCalculator.calculate(emptyList())

        assertEquals(0, result.currentStreak)
        assertEquals(0, result.longestStreak)
        assertNull(result.lastLogDate)
    }

    @Test
    fun singleTodayEntry_returnsStreakOne() {
        val today = LocalDate.now()
        val result = StreakCalculator.calculate(listOf(today))

        assertEquals(1, result.currentStreak)
        assertEquals(1, result.longestStreak)
        assertEquals(today, result.lastLogDate)
    }

    @Test
    fun consecutiveDays_returnsCorrectCurrentStreak() {
        val today = LocalDate.now()
        val dates = listOf(
            today.minusDays(2),
            today.minusDays(1),
            today,
        )

        val result = StreakCalculator.calculate(dates)

        assertEquals(3, result.currentStreak)
        assertEquals(3, result.longestStreak)
    }

    @Test
    fun gap_breaksCurrentStreak() {
        val today = LocalDate.now()
        val dates = listOf(
            today.minusDays(3),
            today.minusDays(1),
            today,
        )

        val result = StreakCalculator.calculate(dates)

        assertEquals(2, result.currentStreak)
        assertEquals(2, result.longestStreak)
    }

    @Test
    fun multipleEntriesSameDay_countedAsOne() {
        val today = LocalDate.now()
        val dates = listOf(today, today, today.minusDays(1), today.minusDays(1))

        val result = StreakCalculator.calculate(dates)

        assertEquals(2, result.currentStreak)
        assertEquals(2, result.longestStreak)
    }

    @Test
    fun futureDates_areIgnored() {
        val today = LocalDate.now()
        val dates = listOf(
            today.plusDays(1),
            today.plusDays(2),
            today,
        )

        val result = StreakCalculator.calculate(dates)

        assertEquals(1, result.currentStreak)
    }

    @Test
    fun longestStreak_calculatedCorrectlyAcrossHistory() {
        val base = LocalDate.now().minusDays(10)
        val dates = listOf(
            base,
            base.plusDays(1),
            base.plusDays(2),
            base.plusDays(5),
            base.plusDays(6),
            base.plusDays(7),
            base.plusDays(8),
        )

        val result = StreakCalculator.calculate(dates)

        assertEquals(4, result.longestStreak)
    }

    @Test
    fun longStreakWithGapAtEnd_currentAndLongestDiffer() {
        val today = LocalDate.now()
        val dates = listOf(
            today.minusDays(5),
            today.minusDays(4),
            today.minusDays(3),
            today.minusDays(1),
            today,
        )

        val result = StreakCalculator.calculate(dates)

        assertEquals(2, result.currentStreak)
        assertEquals(3, result.longestStreak)
    }
}
