package com.elyria.app.domain.model.export

import kotlinx.serialization.Serializable

@Serializable
data class ElyriaExportPayload(
    val appName: String = "Elyria",
    val schemaVersion: Int = 1,
    val exportedAt: String,
    val moodEntries: List<ExportMoodEntry>,
    val journalEntries: List<ExportJournalEntry>,
)
