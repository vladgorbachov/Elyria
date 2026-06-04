package com.elyria.app.core.utils

import com.elyria.app.domain.usecase.streak.StreakCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * DST transitions via [ZoneId.of] — Europe/Prague follows EU rules (spring forward / fall back).
 */
class DateUtilsDstTest {

    private val prague: ZoneId = ZoneId.of("Europe/Prague")

    @Test
    fun timestampToLocalDate_fallBack_duplicateWallClock_sameCalendarDay() {
        // 2024-10-27 02:30 happens twice when clocks go 03:00 → 02:00.
        val first = ZonedDateTime.of(2024, 10, 27, 2, 30, 0, 0, prague)
        val second = first.plusHours(1)

        val dateFirst = DateUtils.timestampToLocalDate(first.toInstant().toEpochMilli(), prague)
        val dateSecond = DateUtils.timestampToLocalDate(second.toInstant().toEpochMilli(), prague)

        assertEquals(LocalDate.of(2024, 10, 27), dateFirst)
        assertEquals(dateFirst, dateSecond)
    }

    @Test
    fun timestampToLocalDate_springForward_afterGap_staysOnTransitionDay() {
        // 2024-03-31 02:00–03:00 skipped; 03:30 is valid.
        val afterGap = ZonedDateTime.of(2024, 3, 31, 3, 30, 0, 0, prague)
        val date = DateUtils.timestampToLocalDate(afterGap.toInstant().toEpochMilli(), prague)

        assertEquals(LocalDate.of(2024, 3, 31), date)
    }

    @Test
    fun timestampToLocalDate_instantBeforeTransition_readWithSameZone_unchangedDate() {
        val beforeTransition = ZonedDateTime.of(2024, 10, 27, 1, 0, 0, 0, prague)
        val millis = beforeTransition.toInstant().toEpochMilli()

        val dateAtLogTime = DateUtils.timestampToLocalDate(millis, prague)
        val dateReadLater = DateUtils.timestampToLocalDate(millis, prague)

        assertEquals(LocalDate.of(2024, 10, 27), dateAtLogTime)
        assertEquals(dateAtLogTime, dateReadLater)
    }

    @Test
    fun streakCalculator_fallBackDuplicateDates_countAsSingleDay() {
        val transitionDay = LocalDate.of(2024, 10, 27)
        val streak = StreakCalculator.calculate(
            entries = listOf(transitionDay, transitionDay),
            today = transitionDay,
        )

        assertEquals(1, streak.currentStreak)
        assertEquals(1, streak.longestStreak)
    }
}
