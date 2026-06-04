package com.elyria.app.presentation.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.elyria.app.R
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.domain.model.MoodTrend

@Composable
fun emotionLabel(emotion: EmotionCategory): String {
    return stringResource(
        when (emotion) {
            EmotionCategory.SAD -> R.string.emotion_sad
            EmotionCategory.ANXIOUS -> R.string.emotion_anxious
            EmotionCategory.ANGRY -> R.string.emotion_angry
            EmotionCategory.TIRED -> R.string.emotion_tired
            EmotionCategory.LONELY -> R.string.emotion_lonely
            EmotionCategory.OVERWHELMED -> R.string.emotion_overwhelmed
            EmotionCategory.CALM -> R.string.emotion_calm
            EmotionCategory.HOPEFUL -> R.string.emotion_hopeful
            EmotionCategory.GRATEFUL -> R.string.emotion_grateful
            EmotionCategory.PROUD -> R.string.emotion_proud
            EmotionCategory.PEACEFUL -> R.string.emotion_peaceful
            EmotionCategory.MOTIVATED -> R.string.emotion_motivated
        },
    )
}

@Composable
fun triggerLabel(trigger: MoodTrigger): String {
    return stringResource(
        when (trigger) {
            MoodTrigger.WORK -> R.string.trigger_work
            MoodTrigger.STUDY -> R.string.trigger_study
            MoodTrigger.FAMILY -> R.string.trigger_family
            MoodTrigger.RELATIONSHIPS -> R.string.trigger_relationships
            MoodTrigger.HEALTH -> R.string.trigger_health
            MoodTrigger.SLEEP -> R.string.trigger_sleep
            MoodTrigger.MONEY -> R.string.trigger_money
            MoodTrigger.WEATHER -> R.string.trigger_weather
            MoodTrigger.SOCIAL_MEDIA -> R.string.trigger_social_media
            MoodTrigger.FOOD -> R.string.trigger_food
            MoodTrigger.EXERCISE -> R.string.trigger_exercise
            MoodTrigger.UNKNOWN -> R.string.trigger_unknown
        },
    )
}

@Composable
fun moodLevelLabel(level: MoodLevel): String {
    return stringResource(
        when (level) {
            MoodLevel.VERY_LOW -> R.string.mood_level_very_low
            MoodLevel.LOW -> R.string.mood_level_low
            MoodLevel.NEUTRAL -> R.string.mood_level_neutral
            MoodLevel.GOOD -> R.string.mood_level_good
            MoodLevel.GREAT -> R.string.mood_level_great
        },
    )
}

@Composable
fun moodTrendLabel(trend: MoodTrend): String {
    return stringResource(
        when (trend) {
            MoodTrend.UP -> R.string.insights_trend_up
            MoodTrend.DOWN -> R.string.insights_trend_down
            MoodTrend.STABLE -> R.string.insights_trend_stable
        },
    )
}

fun gardenEmoji(gardenLevel: Int): String {
    return when (gardenLevel) {
        0 -> "🌱"
        1 -> "🌿"
        2 -> "🍃"
        else -> "🌸"
    }
}
