package com.elyria.app.domain.repository

import com.elyria.app.domain.model.MoodEntry
import kotlinx.coroutines.flow.Flow

interface MoodRepository {
    fun observeAll(): Flow<List<MoodEntry>>

    fun observeByDateRange(startMillis: Long, endMillis: Long): Flow<List<MoodEntry>>

    fun observeLatest(): Flow<MoodEntry?>

    suspend fun insert(entry: MoodEntry): Long

    suspend fun deleteAll(): Int
}
