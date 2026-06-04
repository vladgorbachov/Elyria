package com.mindease.app.data.local.mapper

import com.mindease.app.data.local.entity.JournalEntryEntity
import com.mindease.app.domain.model.JournalEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun JournalEntryEntity.toDomain(): JournalEntry {
    return JournalEntry(
        id = id,
        text = text,
        createdAt = createdAt.toLocalDateTime(),
    )
}

fun JournalEntry.toEntity(): JournalEntryEntity {
    return JournalEntryEntity(
        id = id,
        text = text,
        createdAt = createdAt.toEpochMillis(),
    )
}

private fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
}

private fun LocalDateTime.toEpochMillis(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
