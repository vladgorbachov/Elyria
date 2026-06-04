package com.elyria.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elyria.app.data.local.converter.LocalDateTimeConverter
import com.elyria.app.data.local.dao.JournalDao
import com.elyria.app.data.local.dao.MoodDao
import com.elyria.app.data.local.entity.JournalEntryEntity
import com.elyria.app.data.local.entity.MoodEntryEntity

@Database(
    entities = [
        MoodEntryEntity::class,
        JournalEntryEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao
}
