package com.elyria.app.data.repository

import com.elyria.app.data.local.dao.MoodDao
import com.elyria.app.data.local.mapper.toDomain
import com.elyria.app.data.local.mapper.toEntity
import com.elyria.app.di.IoDispatcher
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.repository.MoodRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoodRepositoryImpl @Inject constructor(
    private val moodDao: MoodDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MoodRepository {

    override fun observeAll(): Flow<List<MoodEntry>> {
        return moodDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeByDateRange(startMillis: Long, endMillis: Long): Flow<List<MoodEntry>> {
        return moodDao.observeByDateRange(startMillis, endMillis).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeLatest(): Flow<MoodEntry?> {
        return moodDao.observeLatest().map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun insert(entry: MoodEntry): Long {
        return withContext(ioDispatcher) {
            moodDao.insert(entry.toEntity())
        }
    }

    override suspend fun deleteAll(): Int {
        return withContext(ioDispatcher) {
            moodDao.deleteAll()
        }
    }
}
