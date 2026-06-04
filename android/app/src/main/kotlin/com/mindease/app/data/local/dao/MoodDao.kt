package com.mindease.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindease.app.data.local.entity.MoodEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY logged_at DESC")
    fun observeAll(): Flow<List<MoodEntryEntity>>

    @Query(
        "SELECT * FROM mood_entries WHERE logged_at BETWEEN :startMillis AND :endMillis " +
            "ORDER BY logged_at DESC",
    )
    fun observeByDateRange(startMillis: Long, endMillis: Long): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries ORDER BY logged_at DESC LIMIT 1")
    suspend fun getLatest(): MoodEntryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MoodEntryEntity): Long

    @Query("DELETE FROM mood_entries")
    suspend fun deleteAll(): Int
}
