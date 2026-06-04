package com.elyria.app.domain.usecase.mood

import com.elyria.app.domain.model.DateRange
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.repository.MoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMoodEntriesUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
) {
    operator fun invoke(period: DateRange? = null): Flow<List<MoodEntry>> {
        if (period == null) {
            return moodRepository.observeAll()
        }
        return moodRepository.observeByDateRange(period.startMillis, period.endMillis)
    }
}
