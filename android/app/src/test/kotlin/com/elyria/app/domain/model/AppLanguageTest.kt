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
    fun fromCode_es_returnsSpanish() {
        assertEquals(AppLanguage.SPANISH, AppLanguage.fromCode("es"))
    }

    @Test
    fun fromCode_de_returnsGerman() {
        assertEquals(AppLanguage.GERMAN, AppLanguage.fromCode("de"))
    }

    @Test
    fun fromCode_fr_returnsFrench() {
        assertEquals(AppLanguage.FRENCH, AppLanguage.fromCode("fr"))
    }

    @Test
    fun fromCode_ptBR_returnsPortugueseBr() {
        assertEquals(AppLanguage.PORTUGUESE_BR, AppLanguage.fromCode("pt-BR"))
    }

    @Test
    fun fromCode_pt_returnsPortuguese() {
        assertEquals(AppLanguage.PORTUGUESE, AppLanguage.fromCode("pt"))
    }

    @Test
    fun fromCode_nl_returnsDutch() {
        assertEquals(AppLanguage.DUTCH, AppLanguage.fromCode("nl"))
    }

    @Test
    fun fromCode_unknown_returnsSystem() {
        assertEquals(AppLanguage.SYSTEM, AppLanguage.fromCode("xx"))
    }
}
