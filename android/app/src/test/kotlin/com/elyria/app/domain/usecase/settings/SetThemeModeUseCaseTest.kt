package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.AppThemeMode
import com.elyria.app.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SetThemeModeUseCaseTest {

    private lateinit var settingsRepository: SettingsRepository
    private lateinit var useCase: SetThemeModeUseCase

    @Before
    fun setup() {
        settingsRepository = mockk(relaxed = true)
        useCase = SetThemeModeUseCase(settingsRepository)
    }

    @Test
    fun invoke_callsRepositoryWithSelectedMode() = runTest {
        val result = useCase(AppThemeMode.DARK)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { settingsRepository.setThemeMode(AppThemeMode.DARK) }
    }

    @Test
    fun invoke_repositoryFailure_returnsFailure() = runTest {
        coEvery { settingsRepository.setThemeMode(any()) } throws RuntimeException("datastore error")

        val result = useCase(AppThemeMode.LIGHT)

        assertTrue(result.isFailure)
    }
}
