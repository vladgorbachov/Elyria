package com.elyria.app.domain.model

import java.time.LocalDateTime

data class JournalEntry(
    val id: Long = 0L,
    val text: String,
    val createdAt: LocalDateTime,
)
