package com.elyria.app.domain.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class DateRange(
    val start: LocalDate,
    val end: LocalDate,
) {
    val startMillis: Long
        get() = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    val endMillis: Long
        get() = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun InsightPeriod.toDateRange(today: LocalDate = LocalDate.now()): DateRange {
    val daysBack = when (this) {
        InsightPeriod.WEEK -> 6L
        InsightPeriod.MONTH -> 29L
    }
    return DateRange(start = today.minusDays(daysBack), end = today)
}
