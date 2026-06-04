package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SetAppLanguageUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: SetAppLanguageUseCase

    @Before
    fun setup() {
        settingsRepository = mockk(relaxed = true)
        useCase = SetAppLanguageUseCase(settingsRepository)
    }

    @Test
    fun invoke_callsRepositoryWithSelectedLanguage() = runTest {
        val result = useCase(AppLanguage.UKRAINIAN)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { settingsRepository.setAppLanguage(AppLanguage.UKRAINIAN) }
    }

    @Test
    fun invoke_repositoryFailure_returnsFailure() = runTest {
        coEvery { settingsRepository.setAppLanguage(any()) } throws RuntimeException("datastore error")

        val result = useCase(AppLanguage.ENGLISH)

        assertTrue(result.isFailure)
    }
}
