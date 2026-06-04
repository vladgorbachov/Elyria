package com.elyria.app.domain.usecase.ai

import com.elyria.app.domain.ai.AICompanion
import com.elyria.app.domain.ai.LanguageIdentifier
import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionContext
import com.elyria.app.domain.model.CompanionLanguage
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrend
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GenerateCompanionChatResponseUseCaseTest {

    private val sampleContext = CompanionChatContext(
        latestMood = MoodLevel.NEUTRAL,
        latestMoodNote = null,
        currentStreak = 2,
        recentEntryCount = 3,
        averageMoodScore = 3.0,
        trend = MoodTrend.STABLE,
    )

    @Test
    fun invoke_blankMessage_returnsSafePromptWithoutCallingAi() = runTest {
        val companion = FakeAICompanion()
        val useCase = useCase(companion, FakeLanguageIdentifier(CompanionLanguage.ENGLISH))

        val result = useCase("   ", sampleContext)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().contains("write a few words"))
        assertEquals(null, companion.receivedMessage)
        assertEquals(null, companion.receivedLanguage)
    }

    @Test
    fun invoke_nonEmptyMessage_detectsLanguageAndPassesToAi() = runTest {
        val companion = FakeAICompanion()
        val useCase = useCase(companion, FakeLanguageIdentifier(CompanionLanguage.RUSSIAN))

        val result = useCase("I feel tired today", sampleContext)

        assertTrue(result.isSuccess)
        assertEquals("Gentle response", result.getOrThrow())
        assertEquals("I feel tired today", companion.receivedMessage)
        assertEquals(CompanionLanguage.RUSSIAN, companion.receivedLanguage)
    }

    @Test
    fun invoke_unknownLanguage_fallsBackToEnglish() = runTest {
        val companion = FakeAICompanion()
        val useCase = useCase(companion, FakeLanguageIdentifier(CompanionLanguage.UNKNOWN))

        useCase("Hello", sampleContext)

        assertEquals(CompanionLanguage.ENGLISH, companion.receivedLanguage)
    }

    @Test
    fun invoke_languageDetectionFailure_fallsBackToEnglish() = runTest {
        val companion = FakeAICompanion()
        val useCase = useCase(companion, ThrowingLanguageIdentifier())

        val result = useCase("Hello", sampleContext)

        assertTrue(result.isSuccess)
        assertEquals(CompanionLanguage.ENGLISH, companion.receivedLanguage)
    }

    @Test
    fun invoke_longMessage_trimsBeforeAiCall() = runTest {
        val companion = FakeAICompanion()
        val useCase = useCase(companion, FakeLanguageIdentifier(CompanionLanguage.ENGLISH))
        val longMessage = "a".repeat(600)

        useCase(longMessage, sampleContext)

        assertEquals(500, companion.receivedMessage?.length)
    }

    @Test
    fun invoke_aiFailure_returnsFailure() = runTest {
        val companion = FakeAICompanion(shouldFail = true)
        val useCase = useCase(companion, FakeLanguageIdentifier(CompanionLanguage.ENGLISH))

        val result = useCase("Hello", sampleContext)

        assertTrue(result.isFailure)
    }

    @Test
    fun invoke_passesContextToAi() = runTest {
        val companion = FakeAICompanion()
        val useCase = useCase(companion, FakeLanguageIdentifier(CompanionLanguage.ENGLISH))

        useCase("How are patterns?", sampleContext)

        assertEquals(sampleContext, companion.receivedContext)
    }

    @Test
    fun invoke_russianDetection_producesRussianResponseThroughMockCompanion() = runTest {
        val useCase = useCase(
            com.elyria.app.data.ai.MockAICompanion(),
            FakeLanguageIdentifier(CompanionLanguage.RUSSIAN),
        )

        val result = useCase("Мне сегодня тяжело", sampleContext)

        assertTrue(result.isSuccess)
        val text = result.getOrThrow()
        assertTrue(text.contains("тяжело") || text.contains("вдох") || text.contains("Похоже"))
    }

    private fun useCase(
        companion: AICompanion,
        languageIdentifier: LanguageIdentifier,
    ): GenerateCompanionChatResponseUseCase {
        return GenerateCompanionChatResponseUseCase(
            companion,
            DetectMessageLanguageUseCase(languageIdentifier),
        )
    }

    private class FakeAICompanion(
        private val shouldFail: Boolean = false,
    ) : AICompanion {
        var receivedMessage: String? = null
        var receivedContext: CompanionChatContext? = null
        var receivedLanguage: CompanionLanguage? = null

        override suspend fun generateReflection(context: CompanionContext): String = "Reflection"

        override suspend fun generateChatResponse(
            userMessage: String,
            context: CompanionChatContext,
            language: CompanionLanguage,
        ): String {
            if (shouldFail) error("AI failed")
            receivedMessage = userMessage
            receivedContext = context
            receivedLanguage = language
            return "Gentle response"
        }
    }

    private class FakeLanguageIdentifier(
        private val language: CompanionLanguage,
    ) : LanguageIdentifier {
        override suspend fun identifyLanguage(text: String): CompanionLanguage = language
    }

    private class ThrowingLanguageIdentifier : LanguageIdentifier {
        override suspend fun identifyLanguage(text: String): CompanionLanguage {
            error("Detection failed")
        }
    }
}
