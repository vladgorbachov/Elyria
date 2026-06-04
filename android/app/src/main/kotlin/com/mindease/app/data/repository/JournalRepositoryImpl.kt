package com.mindease.app.data.repository

import com.mindease.app.data.local.dao.JournalDao
import com.mindease.app.data.local.mapper.toDomain
import com.mindease.app.data.local.mapper.toEntity
import com.mindease.app.di.IoDispatcher
import com.mindease.app.domain.model.JournalEntry
import com.mindease.app.domain.repository.JournalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepositoryImpl @Inject constructor(
    private val journalDao: JournalDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : JournalRepository {

    override fun observeAll(): Flow<List<JournalEntry>> {
        return journalDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(entry: JournalEntry): Long {
        return withContext(ioDispatcher) {
            journalDao.insert(entry.toEntity())
        }
    }
}
