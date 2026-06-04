package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.repository.JournalRepository
import com.elyria.app.domain.repository.MoodRepository
import javax.inject.Inject

class DeleteAllUserDataUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
    private val journalRepository: JournalRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            moodRepository.deleteAll()
            journalRepository.deleteAll()
        }
    }
}
