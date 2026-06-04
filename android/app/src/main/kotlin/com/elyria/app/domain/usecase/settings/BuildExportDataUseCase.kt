package com.elyria.app.domain.usecase.settings

import com.elyria.app.domain.model.JournalEntry
import com.elyria.app.domain.model.MoodEntry
import com.elyria.app.domain.model.export.ElyriaExportPayload
import com.elyria.app.domain.model.export.ExportJournalEntry
import com.elyria.app.domain.model.export.ExportMoodEntry
import com.elyria.app.domain.repository.JournalRepository
import com.elyria.app.domain.repository.MoodRepository
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class BuildExportDataUseCase @Inject constructor(
    private val moodRepository: MoodRepository,
    private val journalRepository: JournalRepository,
) {
    suspend operator fun invoke(): Result<String> {
        return runCatching {
            val moodEntries = moodRepository.observeAll().first()
            val journalEntries = journalRepository.observeAll().first()
            if (moodEntries.isEmpty() && journalEntries.isEmpty()) {
                throw NoExportDataException()
            }
            val payload = ElyriaExportPayload(
                exportedAt = LocalDateTime.now().format(ISO_FORMATTER),
                moodEntries = moodEntries.map { it.toExportMoodEntry() },
                journalEntries = journalEntries.map { it.toExportJournalEntry() },
            )
            Json {
                prettyPrint = true
                encodeDefaults = true
            }.encodeToString(payload)
        }
    }

    private fun MoodEntry.toExportMoodEntry(): ExportMoodEntry {
        return ExportMoodEntry(
            id = id,
            moodLevel = moodLevel.name,
            moodScore = moodLevel.score,
            note = note,
            loggedAt = loggedAt.format(ISO_FORMATTER),
            sentimentScore = sentimentScore,
            emotions = emotions.map { it.name },
            triggers = triggers.map { it.name },
        )
    }

    private fun JournalEntry.toExportJournalEntry(): ExportJournalEntry {
        return ExportJournalEntry(
            id = id,
            text = text,
            createdAt = createdAt.format(ISO_FORMATTER),
        )
    }

    private companion object {
        val ISO_FORMATTER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
}

class NoExportDataException : Exception("No local mood or journal data to export")
