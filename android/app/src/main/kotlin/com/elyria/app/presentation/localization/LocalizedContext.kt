package com.elyria.app.presentation.localization

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import com.elyria.app.domain.model.AppLanguage
import java.util.Locale

fun Context.withAppLanguage(language: AppLanguage): Context {
    if (language == AppLanguage.SYSTEM) {
        return this
    }

    val locale = when (language) {
        AppLanguage.ENGLISH -> Locale.forLanguageTag("en")
        AppLanguage.RUSSIAN -> Locale.forLanguageTag("ru")
        AppLanguage.UKRAINIAN -> Locale.forLanguageTag("uk")
        AppLanguage.ROMANIAN -> Locale.forLanguageTag("ro")
        AppLanguage.SPANISH -> Locale.forLanguageTag("es")
        AppLanguage.GERMAN -> Locale.forLanguageTag("de")
        AppLanguage.FRENCH -> Locale.forLanguageTag("fr")
        AppLanguage.PORTUGUESE_BR -> Locale.forLanguageTag("pt-BR")
        AppLanguage.PORTUGUESE -> Locale.forLanguageTag("pt")
        AppLanguage.DUTCH -> Locale.forLanguageTag("nl")
        AppLanguage.SYSTEM -> Locale.getDefault()
    }

    Locale.setDefault(locale)
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    configuration.setLocales(LocaleList(locale))
    return createConfigurationContext(configuration)
}
