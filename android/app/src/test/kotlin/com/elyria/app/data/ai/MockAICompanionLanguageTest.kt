package com.elyria.app.data.ai

import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionLanguage
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.domain.model.MoodTrend
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MockAICompanionLanguageTest {

    private val companion = MockAICompanion()
    private val emptyContext = CompanionChatContext(
        latestMood = null,
        latestMoodNote = null,
        currentStreak = 0,
        recentEntryCount = 0,
        averageMoodScore = null,
        trend = MoodTrend.STABLE,
        topEmotions = emptyList(),
        topTriggers = emptyList(),
    )

    @Test
    fun generateChatResponse_russianStress_returnsRussianText() = runTest {
        val response = companion.generateChatResponse(
            userMessage = "Мне сегодня тяжело и я в стрессе",
            context = emptyContext,
            language = CompanionLanguage.RUSSIAN,
        )

        assertTrue(response.contains("тяжело") || response.contains("вдох"))
    }

    @Test
    fun generateChatResponse_romanianLanguage_returnsRomanianText() = runTest {
        val response = companion.generateChatResponse(
            userMessage = "Mă simt obosit azi",
            context = emptyContext,
            language = CompanionLanguage.ROMANIAN,
        )

        assertTrue(response.contains("Mulțumesc") || response.contains("respirație") || response.contains("greu"))
    }

    @Test
    fun generateChatResponse_sleepTriggerPattern_russian() = runTest {
        val context = emptyContext.copy(topTriggers = listOf(MoodTrigger.SLEEP))
        val response = companion.generateChatResponse(
            userMessage = "hello",
            context = context,
            language = CompanionLanguage.RUSSIAN,
        )

        assertTrue(response.contains("Сон") || response.contains("сон"))
    }

    @Test
    fun generateChatResponse_unknownLanguage_fallsBackToEnglishPhrases() = runTest {
        val response = companion.generateChatResponse(
            userMessage = "hello there friend",
            context = emptyContext,
            language = CompanionLanguage.UNKNOWN,
        )

        assertTrue(response.contains("Thank you") || response.contains("sharing"))
    }
}
