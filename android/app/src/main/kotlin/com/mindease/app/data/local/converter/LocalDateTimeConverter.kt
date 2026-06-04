package com.mindease.app.data.local.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        if (value == null) return null
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun toTimestamp(dateTime: LocalDateTime?): Long? {
        if (dateTime == null) return null
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}
