package com.mindease.app.data.repository

import com.mindease.app.data.local.dao.MoodDao
import com.mindease.app.data.local.mapper.toDomain
import com.mindease.app.data.local.mapper.toEntity
import com.mindease.app.di.IoDispatcher
import com.mindease.app.domain.model.MoodEntry
import com.mindease.app.domain.repository.MoodRepository
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

    override suspend fun insert(entry: MoodEntry): Long {
        return withContext(ioDispatcher) {
            moodDao.insert(entry.toEntity())
        }
    }
}
