package com.elyria.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CompanionLanguageTest {

    @Test
    fun fromCode_en_returnsEnglish() {
        assertEquals(CompanionLanguage.ENGLISH, CompanionLanguage.fromCode("en"))
    }

    @Test
    fun fromCode_ru_returnsRussian() {
        assertEquals(CompanionLanguage.RUSSIAN, CompanionLanguage.fromCode("ru"))
    }

    @Test
    fun fromCode_uk_returnsUkrainian() {
        assertEquals(CompanionLanguage.UKRAINIAN, CompanionLanguage.fromCode("uk"))
    }

    @Test
    fun fromCode_ro_returnsRomanian() {
        assertEquals(CompanionLanguage.ROMANIAN, CompanionLanguage.fromCode("ro"))
    }

    @Test
    fun fromCode_und_returnsUnknown() {
        assertEquals(CompanionLanguage.UNKNOWN, CompanionLanguage.fromCode("und"))
    }

    @Test
    fun fromCode_null_returnsUnknown() {
        assertEquals(CompanionLanguage.UNKNOWN, CompanionLanguage.fromCode(null))
    }

    @Test
    fun fromCode_uppercase_returnsMappedLanguage() {
        assertEquals(CompanionLanguage.ENGLISH, CompanionLanguage.fromCode("EN"))
        assertEquals(CompanionLanguage.RUSSIAN, CompanionLanguage.fromCode("RU"))
    }
}
