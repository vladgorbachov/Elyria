package com.elyria.app.domain.model

enum class AppLanguage(
    val code: String,
) {
    SYSTEM("system"),
    ENGLISH("en"),
    RUSSIAN("ru"),
    UKRAINIAN("uk"),
    ROMANIAN("ro"),
    SPANISH("es"),
    GERMAN("de"),
    FRENCH("fr"),
    PORTUGUESE_BR("pt-BR"),
    PORTUGUESE("pt"),
    DUTCH("nl"),
    ;

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code == code } ?: SYSTEM
        }
    }
}
