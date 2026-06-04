package com.mindease.app.data.local.mapper

import com.mindease.app.data.local.entity.JournalEntryEntity
import com.mindease.app.domain.model.JournalEntry
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId

class JournalEntryMapperTest {

    @Test
    fun toDomain_andToEntity_preserveTextAndTime() {
        val createdAt = LocalDateTime.of(2026, 6, 3, 8, 15)
        val entry = JournalEntry(
            id = 3L,
            text = "Morning reflection",
            createdAt = createdAt,
        )

        val entity = entry.toEntity()
        val restored = entity.toDomain()

        assertEquals(3L, entity.id)
        assertEquals("Morning reflection", restored.text)
        assertEquals(createdAt, restored.createdAt)
    }

    @Test
    fun toDomain_readsStoredMillis() {
        val createdAt = LocalDateTime.of(2025, 12, 1, 0, 0)
        val millis = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val entity = JournalEntryEntity(id = 1L, text = "Note", createdAt = millis)

        assertEquals(createdAt, entity.toDomain().createdAt)
    }
}
