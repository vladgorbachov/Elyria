package com.elyria.app.domain.model.export

import kotlinx.serialization.Serializable

@Serializable
data class ExportMoodEntry(
    val id: Long,
    val moodLevel: String,
    val moodScore: Int,
    val note: String?,
    val loggedAt: String,
    val sentimentScore: Float?,
    val emotions: List<String> = emptyList(),
    val triggers: List<String> = emptyList(),
)
