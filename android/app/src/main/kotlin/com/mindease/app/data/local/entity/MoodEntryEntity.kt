package com.mindease.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "mood_level")
    val moodLevel: Int,
    val note: String? = null,
    @ColumnInfo(name = "logged_at")
    val loggedAt: Long,
    @ColumnInfo(name = "sentiment_score")
    val sentimentScore: Float? = null,
)
