package com.elyria.app.domain.model.export

import kotlinx.serialization.Serializable

@Serializable
data class ExportJournalEntry(
    val id: Long,
    val text: String,
    val createdAt: String,
)
