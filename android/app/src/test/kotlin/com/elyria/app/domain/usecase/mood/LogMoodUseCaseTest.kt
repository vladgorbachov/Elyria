package com.elyria.app.domain.usecase.mood

import com.elyria.app.core.exception.AppException
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.domain.repository.MoodRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LogMoodUseCaseTest {

    private lateinit var moodRepository: MoodRepository
    private lateinit var logMoodUseCase: LogMoodUseCase

    @Before
    fun setup() {
        moodRepository = mockk()
        logMoodUseCase = LogMoodUseCase(moodRepository)
    }

    @Test
    fun logMood_successfullySavesEntryAndReturnsSuccess() = runTest {
        val moodLevel = MoodLevel.GOOD
        val note = "Feeling great"
        coEvery { moodRepository.insert(any()) } returns 1L

        val result = logMoodUseCase(moodLevel, note)

        assertTrue(result.isSuccess)
        result.getOrNull()?.let { entry ->
            assertEquals(moodLevel, entry.moodLevel)
            assertEquals(note, entry.note)
            assertEquals(1L, entry.id)
        }
        coVerify(exactly = 1) { moodRepository.insert(any()) }
    }

    @Test
    fun logMood_withNullNote_stillSucceeds() = runTest {
        coEvery { moodRepository.insert(any()) } returns 1L

        val result = logMoodUseCase(MoodLevel.NEUTRAL, null)

        assertTrue(result.isSuccess)
    }

    @Test
    fun logMood_repositoryFailure_returnsDatabaseException() = runTest {
        coEvery { moodRepository.insert(any()) } throws RuntimeException("disk full")

        val result = logMoodUseCase(MoodLevel.LOW, null)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is AppException.Database)
    }

    @Test
    fun logMood_blankNoteStoredAsNull() = runTest {
        coEvery { moodRepository.insert(any()) } answers {
            assertEquals(null, firstArg<MoodEntry>().note)
            1L
        }

        logMoodUseCase(MoodLevel.LOW, "   ")
    }

    @Test
    fun logMood_withEmotionsAndTriggers_persistsFields() = runTest {
        coEvery { moodRepository.insert(any()) } answers {
            val entry = firstArg<MoodEntry>()
            assertEquals(listOf(EmotionCategory.TIRED), entry.emotions)
            assertEquals(listOf(MoodTrigger.SLEEP), entry.triggers)
            1L
        }

        val result = logMoodUseCase(
            MoodLevel.LOW,
            null,
            emotions = listOf(EmotionCategory.TIRED),
            triggers = listOf(MoodTrigger.SLEEP),
        )

        assertTrue(result.isSuccess)
    }

    @Test
    fun logMood_withoutTags_stillSucceeds() = runTest {
        coEvery { moodRepository.insert(any()) } returns 1L

        val result = logMoodUseCase(MoodLevel.NEUTRAL, null)

        assertTrue(result.isSuccess)
        coVerify {
            moodRepository.insert(
                match { it.emotions.isEmpty() && it.triggers.isEmpty() },
            )
        }
    }
}
