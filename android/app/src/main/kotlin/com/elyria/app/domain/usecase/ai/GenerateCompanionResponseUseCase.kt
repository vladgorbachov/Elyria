package com.elyria.app.domain.usecase.ai

import com.elyria.app.domain.ai.AICompanion
import com.elyria.app.domain.model.CompanionContext
import com.elyria.app.domain.model.Insight
import com.elyria.app.domain.model.MoodEntry
import javax.inject.Inject

class GenerateCompanionResponseUseCase @Inject constructor(
    private val aiCompanion: AICompanion,
) {
    suspend operator fun invoke(
        insight: Insight,
        moodEntries: List<MoodEntry>,
    ): Result<String> {
        if (moodEntries.isEmpty()) {
            return Result.success(EMPTY_HISTORY_MESSAGE)
        }

        val context = CompanionContext(
            averageMoodScore = insight.averageMoodScore.toDouble(),
            trend = insight.trend,
            mostFrequentMood = insight.mostFrequentMood,
            entryCount = moodEntries.size,
            periodStart = insight.periodStart,
            periodEnd = insight.periodEnd,
        )

        return runCatching {
            aiCompanion.generateReflection(context)
        }
    }

    private companion object {
        const val EMPTY_HISTORY_MESSAGE =
            "Once you add a few mood check-ins, Elyria will reflect on your recent patterns here."
    }
}
