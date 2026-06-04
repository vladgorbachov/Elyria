package com.elyria.app.domain.repository

import com.elyria.app.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun observeAll(): Flow<List<JournalEntry>>

    fun observeByDateRange(startMillis: Long, endMillis: Long): Flow<List<JournalEntry>>

    fun observeLatest(): Flow<JournalEntry?>

    suspend fun insert(entry: JournalEntry): Long

    suspend fun deleteAll(): Int
}
