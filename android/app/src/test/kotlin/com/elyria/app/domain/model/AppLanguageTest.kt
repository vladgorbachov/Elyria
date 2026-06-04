package com.elyria.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {

    @Test
    fun fromCode_null_returnsSystem() {
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromCode(null))
    }

    @Test
    fun fromCode_system_returnsSystem() {
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromCode("system"))
    }

    @Test
    fun fromCode_en_returnsEnglish() {
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("en"))
    }

    @Test
    fun fromCode_ru_returnsRussian() {
        assertEquals(AppLanguage.RUSSIAN, AppLanguage.fromCode("ru"))
    }

    @Test
    fun fromCode_uk_returnsUkrainian() {
        assertEquals(AppLanguage.UKRAINIAN, AppLanguage.fromCode("uk"))
    }

    @Test
    fun fromCode_ro_returnsRomanian() {
        assertEquals(AppLanguage.ROMANIAN, AppLanguage.fromCode("ro"))
    }

    @Test
    fun fromCode_unknown_returnsSystem() {
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromCode("fr"))
    }
}
