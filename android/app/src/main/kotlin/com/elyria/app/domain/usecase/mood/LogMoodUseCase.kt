package com.elyria.app.domain.usecase.mood

import com.elyria.app.core.exception.AppException
import com.elyria.app.core.utils.DateUtils
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.domain.repository.MoodRepository
import javax.inject.Inject

class LogMoodUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
) {
    suspend operator fun invoke(
        moodLevel: MoodLevel,
        note: String?,
        emotions: List<EmotionCategory> = emptyList(),
        triggers: List<MoodTrigger> = emptyList(),
    ): Result<MoodEntry> {
        val trimmedNote = note?.trim()?.takeIf { it.isNotEmpty() }
        val entry = MoodEntry(
            moodLevel = moodLevel,
            note = trimmedNote,
            loggedAt = DateUtils.now(),
            sentimentScore = sentimentFromMood(moodLevel),
            emotions = emotions.take(MAX_TAGS),
            triggers = triggers.take(MAX_TAGS),
        )

        return runCatching {
            val rowId = moodRepository.insert(entry)
            entry.copy(id = rowId)
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { error ->
                Result.failure(
                    AppException.Database("Failed to save mood entry", error),
                )
            },
        )
    }

    private fun sentimentFromMood(level: MoodLevel): Float {
        // MVP: map discrete mood score to 0..1 for future on-device NLP.
        return (level.score - 1) / 4f
    }

    private companion object {
        const val MAX_TAGS = 3
    }
}
