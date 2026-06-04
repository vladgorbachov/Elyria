package com.elyria.app.domain.usecase.mood

import com.elyria.app.core.utils.DateUtils
import com.elyria.app.domain.model.StreakInfo
import com.elyria.app.domain.usecase.streak.StreakCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveStreakUseCase @Inject constructor(
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
) {
    operator fun invoke(): Flow<StreakInfo> {
        return observeMoodEntriesUseCase().map { entries ->
            val dates = entries.map { DateUtils.moodEntryToLocalDate(it.loggedAt) }
            StreakCalculator.calculate(dates)
        }
    }
}
