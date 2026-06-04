package com.elyria.app.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object DateUtils {

    private val zone: ZoneId = ZoneId.systemDefault()

    /**
     * Calendar date in the user's zone for a stored UTC instant.
     *
     * Uses offset rules valid **at [timestampMillis]**, not at read time — so a mood
     * logged before a DST change still maps to the same [LocalDate] when read after
     * the transition (java.time behavior).
     *
     * Edge case: wall-clock times in the 02:00–03:00 window on transition days can
     * be ambiguous (fall back) or invalid (spring forward). Streak dedupes by
     * [LocalDate]; optional day-start offset is planned for settings (step 6).
     */
    fun timestampToLocalDate(
        timestampMillis: Long,
        zoneId: ZoneId = zone,
    ): LocalDate {
        return Instant.ofEpochMilli(timestampMillis)
            .atZone(zoneId)
            .toLocalDate()
    }

    fun today(zoneId: ZoneId = zone): LocalDate = LocalDate.now(zoneId)

    fun now(): LocalDateTime = LocalDateTime.now(zone)

    fun moodEntryToLocalDate(loggedAt: LocalDateTime): LocalDate {
        return loggedAt.atZone(zone).toLocalDate()
    }

    fun isSameDay(first: LocalDateTime, second: LocalDateTime): Boolean {
        return moodEntryToLocalDate(first) == moodEntryToLocalDate(second)
    }
}
