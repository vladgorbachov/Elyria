package com.elyria.app.data.local.mapper

import com.elyria.app.data.local.entity.MoodEntryEntity
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun MoodEntryEntity.toDomain(): MoodEntry {
    return MoodEntry(
        id = id,
        moodLevel = moodLevel.toMoodLevel(),
        note = note,
        loggedAt = loggedAt.toLocalDateTime(),
        sentimentScore = sentimentScore,
        emotions = MoodTagJsonCodec.decodeEmotions(emotionsJson),
        triggers = MoodTagJsonCodec.decodeTriggers(triggersJson),
    )
}

fun MoodEntry.toEntity(): MoodEntryEntity {
    return MoodEntryEntity(
        id = id,
        moodLevel = moodLevel.score,
        note = note,
        loggedAt = loggedAt.toEpochMillis(),
        sentimentScore = sentimentScore,
        emotionsJson = MoodTagJsonCodec.encodeEmotions(emotions),
        triggersJson = MoodTagJsonCodec.encodeTriggers(triggers),
    )
}

private fun Int.toMoodLevel(): MoodLevel {
    return MoodLevel.entries.firstOrNull { it.score == this } ?: MoodLevel.NEUTRAL
}

private fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

private fun LocalDateTime.toEpochMillis(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
