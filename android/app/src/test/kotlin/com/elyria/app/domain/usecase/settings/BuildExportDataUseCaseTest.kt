package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.JournalEntry
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.domain.repository.JournalRepository
import com.elyria.app.domain.repository.MoodRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class BuildExportDataUseCaseTest {

    private lateinit var moodRepository: MoodRepository
    private lateinit var journalRepository: JournalRepository
    private lateinit var useCase: BuildExportDataUseCase

    @Before
    fun setup() {
        moodRepository = mockk()
        journalRepository = mockk()
        useCase = BuildExportDataUseCase(moodRepository, journalRepository)
    }

    @Test
    fun invoke_emptyData_returnsFailure() = runTest {
        every { moodRepository.observeAll() } returns flowOf(emptyList())
        every { journalRepository.observeAll() } returns flowOf(emptyList())

        val result = useCase()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NoExportDataException)
    }

    @Test
    fun invoke_moodEntry_appearsInJson() = runTest {
        val entry = MoodEntry(
            id = 7L,
            moodLevel = MoodLevel.GOOD,
            note = "Calm day",
            loggedAt = LocalDateTime.of(2026, 6, 3, 10, 30),
            sentimentScore = 0.4f,
        )
        every { moodRepository.observeAll() } returns flowOf(listOf(entry))
        every { journalRepository.observeAll() } returns flowOf(emptyList())

        val json = useCase().getOrThrow()

        assertTrue(json.contains("\"id\": 7"))
        assertTrue(json.contains("\"moodLevel\": \"GOOD\""))
        assertTrue(json.contains("\"moodScore\": 4"))
        assertTrue(json.contains("Calm day"))
        assertTrue(json.contains("2026-06-03T10:30"))
        assertTrue(json.contains("\"emotions\""))
        assertTrue(json.contains("\"triggers\""))
    }

    @Test
    fun invoke_exportsEmotionAndTriggerKeysInEnglish() = runTest {
        val entry = MoodEntry(
            id = 8L,
            moodLevel = MoodLevel.NEUTRAL,
            loggedAt = LocalDateTime.of(2026, 6, 4, 11, 0),
            emotions = listOf(EmotionCategory.CALM),
            triggers = listOf(MoodTrigger.SLEEP),
        )
        every { moodRepository.observeAll() } returns flowOf(listOf(entry))
        every { journalRepository.observeAll() } returns flowOf(emptyList())

        val json = useCase().getOrThrow()

        assertTrue(json.contains("\"CALM\""))
        assertTrue(json.contains("\"SLEEP\""))
    }

    @Test
    fun invoke_journalEntry_appearsInJson() = runTest {
        val entry = JournalEntry(
            id = 3L,
            text = "Morning reflection",
            createdAt = LocalDateTime.of(2026, 6, 2, 8, 0),
        )
        every { moodRepository.observeAll() } returns flowOf(emptyList())
        every { journalRepository.observeAll() } returns flowOf(listOf(entry))

        val json = useCase().getOrThrow()

        assertTrue(json.contains("\"id\": 3"))
        assertTrue(json.contains("Morning reflection"))
        assertTrue(json.contains("2026-06-02T08:00"))
    }

    @Test
    fun invoke_doesNotIncludeDeviceIdentifiers() = runTest {
        val mood = MoodEntry(
            id = 1L,
            moodLevel = MoodLevel.NEUTRAL,
            loggedAt = LocalDateTime.of(2026, 6, 1, 12, 0),
        )
        every { moodRepository.observeAll() } returns flowOf(listOf(mood))
        every { journalRepository.observeAll() } returns flowOf(emptyList())

        val json = useCase().getOrThrow().lowercase()

        assertFalse(json.contains("deviceid"))
        assertFalse(json.contains("android_id"))
        assertFalse(json.contains("install"))
        assertFalse(json.contains("analytics"))
    }

    @Test
    fun invoke_includesExportedAtAndAppMetadata() = runTest {
        val mood = MoodEntry(
            id = 1L,
            moodLevel = MoodLevel.LOW,
            loggedAt = LocalDateTime.of(2026, 6, 1, 9, 0),
        )
        every { moodRepository.observeAll() } returns flowOf(listOf(mood))
        every { journalRepository.observeAll() } returns flowOf(emptyList())

        val json = useCase().getOrThrow()

        assertTrue(json.contains("\"appName\": \"Elyria\""))
        assertTrue(json.contains("\"schemaVersion\": 1"))
        assertTrue(json.contains("\"exportedAt\""))
    }
}
