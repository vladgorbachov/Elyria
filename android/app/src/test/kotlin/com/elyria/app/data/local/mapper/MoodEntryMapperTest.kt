package com.elyria.app.data.local.mapper

import com.elyria.app.data.local.entity.MoodEntryEntity
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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

    @Test
    fun toDomain_roundTripsEmotionsAndTriggers() {
        val entity = MoodEntryEntity(
            id = 3L,
            moodLevel = MoodLevel.GOOD.score,
            loggedAt = 1L,
            emotionsJson = MoodTagJsonCodec.encodeEmotions(
                listOf(EmotionCategory.CALM, EmotionCategory.GRATEFUL),
            ),
            triggersJson = MoodTagJsonCodec.encodeTriggers(listOf(MoodTrigger.SLEEP)),
        )

        val domain = entity.toDomain()

        assertEquals(listOf(EmotionCategory.CALM, EmotionCategory.GRATEFUL), domain.emotions)
        assertEquals(listOf(MoodTrigger.SLEEP), domain.triggers)
    }

    @Test
    fun toDomain_badJson_returnsEmptyLists() {
        val entity = MoodEntryEntity(
            moodLevel = MoodLevel.NEUTRAL.score,
            loggedAt = 0L,
            emotionsJson = "{not-json",
            triggersJson = "INVALID",
        )

        val domain = entity.toDomain()

        assertTrue(domain.emotions.isEmpty())
        assertTrue(domain.triggers.isEmpty())
    }

    @Test
    fun toDomain_unknownEnumValues_areIgnored() {
        val entity = MoodEntryEntity(
            moodLevel = MoodLevel.NEUTRAL.score,
            loggedAt = 0L,
            emotionsJson = """["SAD","NOT_A_REAL_EMOTION"]""",
            triggersJson = """["SLEEP","FAKE_TRIGGER"]""",
        )

        val domain = entity.toDomain()

        assertEquals(listOf(EmotionCategory.SAD), domain.emotions)
        assertEquals(listOf(MoodTrigger.SLEEP), domain.triggers)
    }
}
