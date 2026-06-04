package com.elyria.app.domain.model

enum class AppLanguage(
    val code: String,
) {
    SYSTEM("system"),
    ENGLISH("en"),
    RUSSIAN("ru"),
    UKRAINIAN("uk"),
    ROMANIAN("ro"),
    ;

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code == code } ?: SYSTEM
        }
    }
}
