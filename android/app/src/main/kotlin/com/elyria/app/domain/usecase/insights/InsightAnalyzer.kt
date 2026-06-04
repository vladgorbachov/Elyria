package com.elyria.app.domain.usecase.insights

import com.elyria.app.domain.model.DateRange
import com.elyria.app.domain.model.Insight
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrend
import kotlin.math.abs
import kotlin.math.roundToInt

internal object InsightAnalyzer {

    fun buildInsight(entries: List<MoodEntry>, range: DateRange): Insight {
        if (entries.isEmpty()) {
            return emptyInsight(range)
        }

        val average = entries.map { it.moodLevel.score }.average().toFloat()
        val trend = resolveTrend(entries)
        val frequentMood = mostFrequentMood(entries)

        return Insight(
            summary = buildSummary(average, trend),
            periodStart = range.start,
            periodEnd = range.end,
            averageMoodScore = average,
            trend = trend,
            mostFrequentMood = frequentMood,
        )
    }

    private fun emptyInsight(range: DateRange): Insight {
        return Insight(
            summary = "Log your mood to see insights for this period.",
            periodStart = range.start,
            periodEnd = range.end,
            averageMoodScore = 0f,
            trend = MoodTrend.STABLE,
            mostFrequentMood = null,
        )
    }

    private fun resolveTrend(entries: List<MoodEntry>): MoodTrend {
        if (entries.size < 2) return MoodTrend.STABLE

        val sorted = entries.sortedBy { it.loggedAt }
        val midpoint = sorted.size / 2
        val earlier = sorted.take(midpoint).map { it.moodLevel.score }
        val later = sorted.drop(midpoint).map { it.moodLevel.score }
        if (earlier.isEmpty() || later.isEmpty()) return MoodTrend.STABLE

        val delta = later.average() - earlier.average()
        return when {
            delta > 0.25 -> MoodTrend.UP
            delta < -0.25 -> MoodTrend.DOWN
            else -> MoodTrend.STABLE
        }
    }

    private fun mostFrequentMood(entries: List<MoodEntry>): MoodLevel? {
        return entries
            .groupingBy { it.moodLevel }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }

    private fun buildSummary(average: Float, trend: MoodTrend): String {
        val roundedAverage = (average * 10f).roundToInt() / 10f
        val trendPhrase = when (trend) {
            MoodTrend.UP -> "improved"
            MoodTrend.DOWN -> "declined"
            MoodTrend.STABLE -> "stayed steady"
        }
        val percent = when (trend) {
            MoodTrend.STABLE -> null
            MoodTrend.UP -> ((average - 3f) * 12f).roundToInt().coerceAtLeast(1)
            MoodTrend.DOWN -> ((3f - average) * 12f).roundToInt().coerceAtLeast(1)
        }

        return if (percent == null || trend == MoodTrend.STABLE) {
            "Your mood $trendPhrase this period (avg $roundedAverage / 5)."
        } else {
            "Your mood $trendPhrase by about ${abs(percent)}% (avg $roundedAverage / 5)."
        }
    }
}
