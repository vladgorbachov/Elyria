package com.elyria.app.domain.usecase.insights

import app.cash.turbine.test
import com.elyria.app.domain.model.InsightPeriod
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrend
import com.elyria.app.domain.model.toDateRange
import com.elyria.app.domain.repository.MoodRepository
import com.elyria.app.domain.usecase.mood.ObserveMoodEntriesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GetInsightsUseCaseTest {

    private lateinit var moodRepository: MoodRepository
    private lateinit var getInsightsUseCase: GetInsightsUseCase

    @Before
    fun setup() {
        moodRepository = mockk()
        getInsightsUseCase = GetInsightsUseCase(ObserveMoodEntriesUseCase(moodRepository))
    }

    @Test
    fun insightsForEmptyData_returnsNeutralValues() = runTest {
        every { moodRepository.observeByDateRange(any<Long>(), any<Long>()) } returns flowOf(emptyList())

        getInsightsUseCase(InsightPeriod.WEEK).test {
            val insight = awaitItem()
            assertEquals(0f, insight.averageMoodScore)
            assertEquals(MoodTrend.STABLE, insight.trend)
            assertNull(insight.mostFrequentMood)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insights_calculatesAverageAndTrendCorrectly() = runTest {
        val now = LocalDateTime.now()
        val entries = listOf(
            MoodEntry(1, MoodLevel.VERY_LOW, null, now.minusDays(3)),
            MoodEntry(2, MoodLevel.GOOD, null, now.minusDays(2)),
            MoodEntry(3, MoodLevel.GREAT, null, now.minusDays(1)),
        )
        every { moodRepository.observeByDateRange(any<Long>(), any<Long>()) } returns flowOf(entries)

        getInsightsUseCase(InsightPeriod.WEEK).test {
            val insight = awaitItem()
            assertEquals(3.33f, insight.averageMoodScore, 0.01f)
            assertEquals(MoodTrend.UP, insight.trend)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun buildInsight_singleEntry_isStableTrend() {
        val today = LocalDate.now()
        val range = InsightPeriod.WEEK.toDateRange(today)
        val entries = listOf(
            MoodEntry(1, MoodLevel.NEUTRAL, null, today.atTime(10, 0)),
        )

        val insight = InsightAnalyzer.buildInsight(entries, range)

        assertEquals(MoodTrend.STABLE, insight.trend)
        assertEquals(MoodLevel.NEUTRAL, insight.mostFrequentMood)
    }
}
