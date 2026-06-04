package com.elyria.app.domain.usecase.insights

import com.elyria.app.domain.model.Insight
import com.elyria.app.domain.model.InsightPeriod
import com.elyria.app.domain.model.toDateRange
import com.elyria.app.domain.usecase.mood.ObserveMoodEntriesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInsightsUseCase @Inject constructor(
    private val observeMoodEntriesUseCase: ObserveMoodEntriesUseCase,
) {
    operator fun invoke(period: InsightPeriod): Flow<Insight> {
        val range = period.toDateRange()
        return observeMoodEntriesUseCase(range).map { entries ->
            InsightAnalyzer.buildInsight(entries, range)
        }
    }
}
