package com.mindease.app.domain.usecase.practice

import com.mindease.app.domain.model.Practice
import com.mindease.app.domain.repository.PracticeRepository
import javax.inject.Inject

class GetPracticesUseCase @Inject constructor(
    private val practiceRepository: PracticeRepository,
) {
    operator fun invoke(): List<Practice> = practiceRepository.getAll()
}
