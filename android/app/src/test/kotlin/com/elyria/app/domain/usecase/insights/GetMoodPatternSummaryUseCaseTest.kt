package com.elyria.app.domain.usecase.insights

import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class GetMoodPatternSummaryUseCaseTest {

    private val useCase = GetMoodPatternSummaryUseCase()

    @Test
    fun invoke_emptyEntries_returnsEmptySummary() {
        val summary = useCase(emptyList())

        assertEquals(0, summary.entryCount)
        assertTrue(summary.topEmotions.isEmpty())
        assertTrue(summary.topTriggers.isEmpty())
    }

    @Test
    fun invoke_countsTopEmotions() {
        val entries = listOf(
            entry(MoodLevel.GOOD, emotions = listOf(EmotionCategory.CALM, EmotionCategory.GRATEFUL)),
            entry(MoodLevel.NEUTRAL, emotions = listOf(EmotionCategory.CALM)),
        )

        val summary = useCase(entries)

        assertEquals(EmotionCategory.CALM, summary.topEmotions.first())
        assertTrue(summary.topEmotions.contains(EmotionCategory.GRATEFUL))
    }

    @Test
    fun invoke_countsTopTriggers() {
        val entries = listOf(
            entry(MoodLevel.GOOD, triggers = listOf(MoodTrigger.SLEEP)),
            entry(MoodLevel.NEUTRAL, triggers = listOf(MoodTrigger.SLEEP, MoodTrigger.WORK)),
        )

        val summary = useCase(entries)

        assertEquals(MoodTrigger.SLEEP, summary.topTriggers.first())
    }

    @Test
    fun invoke_detectsLowMoodTriggers() {
        val entries = listOf(
            entry(MoodLevel.LOW, triggers = listOf(MoodTrigger.SLEEP)),
            entry(MoodLevel.VERY_LOW, triggers = listOf(MoodTrigger.SLEEP)),
            entry(MoodLevel.GOOD, triggers = listOf(MoodTrigger.WORK)),
        )

        val summary = useCase(entries)

        assertTrue(summary.lowMoodTriggers.contains(MoodTrigger.SLEEP))
    }

    private fun entry(
        level: MoodLevel,
        emotions: List<EmotionCategory> = emptyList(),
        triggers: List<MoodTrigger> = emptyList(),
    ): MoodEntry {
        return MoodEntry(
            id = 1L,
            moodLevel = level,
            loggedAt = LocalDateTime.of(2026, 6, 3, 12, 0),
            emotions = emotions,
            triggers = triggers,
        )
    }
}
