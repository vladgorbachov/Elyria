package com.elyria.app.domain.usecase.insights

import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodPatternSummary
import com.elyria.app.domain.model.MoodTrigger
import javax.inject.Inject

class GetMoodPatternSummaryUseCase @Inject constructor() {

    operator fun invoke(entries: List<MoodEntry>): MoodPatternSummary {
        if (entries.isEmpty()) {
            return MoodPatternSummary(
                topEmotions = emptyList(),
                topTriggers = emptyList(),
                lowMoodTriggers = emptyList(),
                entryCount = 0,
            )
        }

        val emotionCounts = mutableMapOf<EmotionCategory, Int>()
        val triggerCounts = mutableMapOf<MoodTrigger, Int>()
        val lowMoodTriggerCounts = mutableMapOf<MoodTrigger, Int>()

        entries.forEach { entry ->
            entry.emotions.forEach { emotion ->
                emotionCounts[emotion] = (emotionCounts[emotion] ?: 0) + 1
            }
            entry.triggers
                .filter { it != MoodTrigger.UNKNOWN }
                .forEach { trigger ->
                    triggerCounts[trigger] = (triggerCounts[trigger] ?: 0) + 1
                }
            if (entry.moodLevel == MoodLevel.LOW || entry.moodLevel == MoodLevel.VERY_LOW) {
                entry.triggers
                    .filter { it != MoodTrigger.UNKNOWN }
                    .forEach { trigger ->
                        lowMoodTriggerCounts[trigger] = (lowMoodTriggerCounts[trigger] ?: 0) + 1
                    }
            }
        }

        return MoodPatternSummary(
            topEmotions = topKeys(emotionCounts, TOP_COUNT),
            topTriggers = topKeys(triggerCounts, TOP_COUNT),
            lowMoodTriggers = topKeys(lowMoodTriggerCounts, TOP_COUNT),
            entryCount = entries.size,
        )
    }

    private fun <T> topKeys(counts: Map<T, Int>, limit: Int): List<T> {
        return counts.entries
            .sortedByDescending { it.value }
            .take(limit)
            .map { it.key }
    }

    private companion object {
        const val TOP_COUNT = 3
    }
}
