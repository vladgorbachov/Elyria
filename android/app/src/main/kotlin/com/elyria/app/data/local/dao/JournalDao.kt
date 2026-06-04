package com.elyria.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elyria.app.data.local.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY created_at DESC")
    fun observeAll(): Flow<List<JournalEntryEntity>>

    @Query(
        "SELECT * FROM journal_entries WHERE created_at BETWEEN :startMillis AND :endMillis " +
            "ORDER BY created_at DESC",
    )
    fun observeByDateRange(startMillis: Long, endMillis: Long): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries ORDER BY created_at DESC LIMIT 1")
    fun observeLatest(): Flow<JournalEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: JournalEntryEntity): Long

    @Query("DELETE FROM journal_entries")
    suspend fun deleteAll(): Int
}
