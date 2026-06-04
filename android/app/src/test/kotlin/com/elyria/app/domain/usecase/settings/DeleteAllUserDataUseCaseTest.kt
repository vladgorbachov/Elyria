package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.repository.JournalRepository
import com.elyria.app.domain.repository.MoodRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteAllUserDataUseCaseTest {

    private lateinit var moodRepository: MoodRepository
    private lateinit var journalRepository: JournalRepository
    private lateinit var useCase: DeleteAllUserDataUseCase

    @Before
    fun setup() {
        moodRepository = mockk(relaxed = true)
        journalRepository = mockk(relaxed = true)
        useCase = DeleteAllUserDataUseCase(moodRepository, journalRepository)
    }

    @Test
    fun invoke_deletesMoodAndJournalData() = runTest {
        val result = useCase()

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { moodRepository.deleteAll() }
        coVerify(exactly = 1) { journalRepository.deleteAll() }
    }

    @Test
    fun invoke_moodRepositoryFailure_returnsFailure() = runTest {
        coEvery { moodRepository.deleteAll() } throws RuntimeException("db error")

        val result = useCase()

        assertTrue(result.isFailure)
    }
}
