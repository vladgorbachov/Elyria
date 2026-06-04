package com.elyria.app.domain.usecase.mood

import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestMoodUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
) {
    operator fun invoke(): Flow<MoodEntry?> = moodRepository.observeLatest()
}
