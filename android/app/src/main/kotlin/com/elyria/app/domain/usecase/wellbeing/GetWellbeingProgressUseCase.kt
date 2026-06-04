package com.elyria.app.domain.usecase.wellbeing

import com.elyria.app.domain.model.InsightPeriod
import com.elyria.app.domain.model.WellbeingProgress
import com.elyria.app.domain.model.toDateRange
import com.elyria.app.domain.usecase.mood.ObserveMoodEntriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWellbeingProgressUseCase @Inject constructor(
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
) {
    operator fun invoke(): Flow<WellbeingProgress> {
        return observeMoodEntriesUseCase(InsightPeriod.WEEK.toDateRange()).map { entries ->
            val checkIns = entries.size
            WellbeingProgress(
                checkInsThisWeek = checkIns,
                practicesThisWeek = 0,
                reflectionCountThisWeek = 0,
                gardenLevel = gardenLevelFromCheckIns(checkIns),
            )
        }
    }

    private fun gardenLevelFromCheckIns(checkIns: Int): Int {
        return when {
            checkIns >= 7 -> 3
            checkIns >= 4 -> 2
            checkIns >= 1 -> 1
            else -> 0
        }
    }
}
