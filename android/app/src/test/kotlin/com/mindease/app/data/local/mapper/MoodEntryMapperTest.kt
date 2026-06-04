package com.mindease.app.data.local.mapper

import com.mindease.app.data.local.entity.MoodEntryEntity
import com.mindease.app.domain.model.MoodEntry
import com.mindease.app.domain.model.MoodLevel
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId

class MoodEntryMapperTest {

    @Test
    fun toDomain_mapsMoodLevelAndTimestamp() {
        val instant = LocalDateTime.of(2026, 6, 3, 12, 0)
        val millis = instant.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val entity = MoodEntryEntity(
            id = 1L,
            moodLevel = MoodLevel.GOOD.score,
            note = "Calm",
            loggedAt = millis,
            sentimentScore = 0.5f,
        )

        val domain = entity.toDomain()

        assertEquals(1L, domain.id)
        assertEquals(MoodLevel.GOOD, domain.moodLevel)
        assertEquals("Calm", domain.note)
        assertEquals(instant, domain.loggedAt)
        assertEquals(0.5f, domain.sentimentScore)
    }

    @Test
    fun toEntity_roundTripsIdAndScore() {
        val entry = MoodEntry(
            id = 2L,
            moodLevel = MoodLevel.NEUTRAL,
            note = null,
            loggedAt = LocalDateTime.of(2026, 1, 1, 9, 30),
        )

        val entity = entry.toEntity()

        assertEquals(2L, entity.id)
        assertEquals(MoodLevel.NEUTRAL.score, entity.moodLevel)
        assertEquals(entry.loggedAt, entity.toDomain().loggedAt)
    }

    @Test
    fun toDomain_unknownMoodLevel_defaultsToNeutral() {
        val entity = MoodEntryEntity(
            moodLevel = 99,
            loggedAt = 0L,
        )

        assertEquals(MoodLevel.NEUTRAL, entity.toDomain().moodLevel)
    }
}
