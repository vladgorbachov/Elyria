package com.elyria.app.data.local.mapper

import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodTrigger
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

object MoodTagJsonCodec {

    private val json = Json { ignoreUnknownKeys = true }
    private val stringListSerializer = ListSerializer(String.serializer())

    fun encodeEmotions(emotions: List<EmotionCategory>): String {
        return encodeEnumNames(emotions.map { it.name })
    }

    fun encodeTriggers(triggers: List<MoodTrigger>): String {
        return encodeEnumNames(triggers.map { it.name })
    }

    fun decodeEmotions(raw: String?): List<EmotionCategory> {
        return decodeEnumNames(raw) { name ->
            EmotionCategory.entries.firstOrNull { it.name == name }
        }
    }

    fun decodeTriggers(raw: String?): List<MoodTrigger> {
        return decodeEnumNames(raw) { name ->
            MoodTrigger.entries.firstOrNull { it.name == name }
        }
    }

    private fun encodeEnumNames(names: List<String>): String {
        if (names.isEmpty()) return "[]"
        return json.encodeToString(stringListSerializer, names)
    }

    private fun <T> decodeEnumNames(raw: String?, resolve: (String) -> T?): List<T> {
        if (raw.isNullOrBlank()) return emptyList()
        return try {
            json.decodeFromString(stringListSerializer, raw)
                .mapNotNull(resolve)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
