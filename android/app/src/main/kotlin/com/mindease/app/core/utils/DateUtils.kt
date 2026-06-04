package com.mindease.app.core.utils

import java.time.LocalDate
import java.time.LocalDateTime

object DateUtils {
    fun today(): LocalDate = LocalDate.now()

    fun now(): LocalDateTime = LocalDateTime.now()

    fun isSameDay(first: LocalDateTime, second: LocalDateTime): Boolean {
        return first.toLocalDate() == second.toLocalDate()
    }
}
