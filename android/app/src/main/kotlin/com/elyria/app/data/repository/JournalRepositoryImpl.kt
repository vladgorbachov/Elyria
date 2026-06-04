package com.elyria.app.data.repository

import com.elyria.app.data.local.dao.JournalDao
import com.elyria.app.data.local.mapper.toDomain
import com.elyria.app.data.local.mapper.toEntity
import com.elyria.app.di.IoDispatcher
import com.elyria.app.domain.model.JournalEntry
import com.elyria.app.domain.repository.JournalRepository
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

    override fun observeByDateRange(startMillis: Long, endMillis: Long): Flow<List<JournalEntry>> {
        return journalDao.observeByDateRange(startMillis, endMillis).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeLatest(): Flow<JournalEntry?> {
        return journalDao.observeLatest().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun insert(entry: JournalEntry): Long {
        return withContext(ioDispatcher) {
            journalDao.insert(entry.toEntity())
        }
    }

    override suspend fun deleteAll(): Int {
        return withContext(ioDispatcher) {
            journalDao.deleteAll()
        }
    }
}
