package com.elyria.app.domain.usecase.ai

import com.elyria.app.data.ai.MockAICompanion
import com.elyria.app.domain.ai.AICompanion
import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionContext
import com.elyria.app.domain.model.CompanionLanguage
import com.elyria.app.domain.model.Insight
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrend
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class GenerateCompanionResponseUseCaseTest {

    private val periodStart = LocalDate.of(2026, 6, 1)
    private val periodEnd = LocalDate.of(2026, 6, 7)

    @Test
    fun invoke_emptyMoodList_returnsEmptyHistoryMessage() = runTest {
        val useCase = GenerateCompanionResponseUseCase(FakeAICompanion())
        val insight = sampleInsight(MoodTrend.STABLE, 3f)

        val result = useCase(insight, emptyList())

        assertTrue(result.isSuccess)
        assertTrue(
            result.getOrThrow().contains("Once you add a few mood check-ins"),
        )
    }

    @Test
    fun invoke_upwardTrend_returnsNonEmptyReflection() = runTest {
        val useCase = GenerateCompanionResponseUseCase(
            FakeAICompanion(response = "Your mood seems to be improving over this period."),
        )
        val entries = listOf(entry(MoodLevel.GOOD))

        val result = useCase(sampleInsight(MoodTrend.UP, 4.2f), entries)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isNotBlank())
    }

    @Test
    fun invoke_downwardTrend_doesNotContainClinicalLanguage() = runTest {
        val useCase = GenerateCompanionResponseUseCase(MockAICompanion())
        val entries = listOf(entry(MoodLevel.LOW))

        val result = useCase(sampleInsight(MoodTrend.DOWN, 2f), entries)
        val text = result.getOrThrow().lowercase()

        assertFalse(text.contains("depressed"))
        assertFalse(text.contains("anxiety"))
        assertFalse(text.contains("diagnos"))
    }

    @Test
    fun invoke_companionFailure_returnsFailure() = runTest {
        val useCase = GenerateCompanionResponseUseCase(FakeAICompanion(shouldFail = true))
        val entries = listOf(entry(MoodLevel.NEUTRAL))

        val result = useCase(sampleInsight(MoodTrend.STABLE, 3f), entries)

        assertTrue(result.isFailure)
    }

    @Test
    fun invoke_passesInsightFieldsIntoCompanionContext() = runTest {
        val companion = RecordingAICompanion()
        val useCase = GenerateCompanionResponseUseCase(companion)
        val entries = listOf(entry(MoodLevel.GREAT), entry(MoodLevel.GOOD))
        val insight = sampleInsight(MoodTrend.UP, 4.5f, MoodLevel.GREAT)

        useCase(insight, entries)

        val context = companion.lastContext
        requireNotNull(context)
        assertEquals(4.5, context.averageMoodScore, 0.01)
        assertEquals(MoodTrend.UP, context.trend)
        assertEquals(MoodLevel.GREAT, context.mostFrequentMood)
        assertEquals(2, context.entryCount)
        assertEquals(periodStart, context.periodStart)
        assertEquals(periodEnd, context.periodEnd)
    }

    private fun sampleInsight(
        trend: MoodTrend,
        average: Float,
        frequent: MoodLevel? = MoodLevel.NEUTRAL,
    ): Insight {
        return Insight(
            summary = "Summary",
            periodStart = periodStart,
            periodEnd = periodEnd,
            averageMoodScore = average,
            trend = trend,
            mostFrequentMood = frequent,
        )
    }

    private fun entry(level: MoodLevel): MoodEntry {
        return MoodEntry(
            id = 1L,
            moodLevel = level,
            loggedAt = LocalDateTime.of(2026, 6, 3, 12, 0),
        )
    }

    private class FakeAICompanion(
        private val response: String = "Your mood looks mostly stable across this period.",
        private val shouldFail: Boolean = false,
    ) : AICompanion {
        override suspend fun generateReflection(context: CompanionContext): String {
            if (shouldFail) error("AI failed")
            return response
        }

        override suspend fun generateChatResponse(
            userMessage: String,
            context: CompanionChatContext,
            language: CompanionLanguage,
        ): String = "Chat"
    }

    private class RecordingAICompanion : AICompanion {
        var lastContext: CompanionContext? = null

        override suspend fun generateReflection(context: CompanionContext): String {
            lastContext = context
            return "Recorded"
        }

        override suspend fun generateChatResponse(
            userMessage: String,
            context: CompanionChatContext,
            language: CompanionLanguage,
        ): String = "Chat"
    }
}
