package com.elyria.app.domain.model

enum class CompanionLanguage(
    val code: String,
) {
    ENGLISH("en"),
    RUSSIAN("ru"),
    UKRAINIAN("uk"),
    ROMANIAN("ro"),
    UNKNOWN("unknown"),
    ;

    companion object {
        fun fromCode(code: String?): CompanionLanguage {
            return when (code?.lowercase()) {
                "en" -> ENGLISH
                "ru" -> RUSSIAN
                "uk" -> UKRAINIAN
                "ro" -> ROMANIAN
                "und", "unknown", null -> UNKNOWN
                else -> UNKNOWN
            }
        }
    }
}
