package com.mindease.app.domain.repository

import com.mindease.app.domain.model.MoodEntry
import kotlinx.coroutines.flow.Flow

interface MoodRepository {
    fun observeAll(): Flow<List<MoodEntry>>
    suspend fun insert(entry: MoodEntry): Long
}
