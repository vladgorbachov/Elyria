package com.mindease.app.domain.repository

import com.mindease.app.domain.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun observeAll(): Flow<List<JournalEntry>>
    suspend fun insert(entry: JournalEntry): Long
}
