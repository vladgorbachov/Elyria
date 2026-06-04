package com.elyria.app.domain.usecase.practice

import com.elyria.app.domain.model.Practice
import com.elyria.app.domain.repository.PracticeRepository
import javax.inject.Inject

class GetPracticesUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    operator fun invoke(): List<Practice> = practiceRepository.getAll()
}
