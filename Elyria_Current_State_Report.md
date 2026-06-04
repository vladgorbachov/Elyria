# Elyria Android — Current State Report

**Date:** 2026-06-03  
**App version:** 1.0.0 (versionCode 1)  
**Application ID:** `com.elyria.app`  
**Architecture:** Clean Architecture (domain / data / presentation), Hilt, Jetpack Compose, Room, DataStore  
**Build status:** `./gradlew test assembleDebug` — **BUILD SUCCESSFUL**

---

## 1. Executive summary

Elyria is a privacy-first Android wellbeing app: mood logging with emotions/triggers, mindfulness practices, local insights, on-device AI reflection and companion chat. All user data stays on device; no network permission. Step **6.L** adds a Settings language selector (System / EN / RU / UK / RO), global UI locale via localized `Context`, full string-resource localization, and `UiText` for ViewModel messages.

---

## 2. Completed steps

| Step | Status |
|------|--------|
| Step 0 — project scaffold | Done |
| Step 1 — theme, navigation shell | Done |
| Step 2 — mood log + Room | Done |
| Step 3 — practices + streak | Done |
| Step 4 — insights charts | Done |
| Step 5.1 — Insights AI reflection | Done |
| Step 5.2 — in-memory CompanionChat | Done |
| Step 5.3 — language-aware companion (ML Kit) | Done |
| Step 6.1 — Settings foundation | Done |
| Step 6.2 — theme, export JSON, delete all data | Done |
| **Step 6.L** — app language + localization cleanup | **Done** |
| Step 7.1 — emotions, triggers, patterns, Inner Garden | Done (ahead of schedule) |
| Step 6.3 — Crisis resources screen | **Pending** |
| Step 6.4 — WorkManager reminders | **Pending** |

---

## 3. Current feature map

| Area | Features |
|------|----------|
| Home | Streak, latest mood, Inner Garden, quick mood, companion card, suggested practice |
| Mood | 5-level mood, up to 3 emotions + 3 triggers, note, Room persistence |
| Practices | Catalog, Lottie cards, timer detail screen |
| Insights | Week/month, chart, pattern summary, AI reflection, ask companion |
| Companion | In-memory chat, ML Kit language detection, pattern-aware mock AI |
| Settings | Theme, **app language**, export JSON, delete mood/journal, privacy note |
| Onboarding | Disclaimer, complete flag in DataStore |

---

## 4. Architecture overview

```
presentation/  → ViewModels, Compose UI, NavGraph, localization (LocalizedContext)
domain/        → models, repository interfaces, use cases (no Android imports)
data/          → Room, DataStore, MockAICompanion, MlKitLanguageIdentifier
di/            → Hilt modules
```

**AppLanguage vs CompanionLanguage**

- `AppLanguage` — UI locale from Settings → DataStore `app_language` → `Context.withAppLanguage()` at `ElyriaApp` root.
- `CompanionLanguage` — detected per user message via ML Kit; controls companion reply language only.

---

## 5. Data layer

### DataStore keys (`UserPreferences`)

| Key | Type | Default / notes |
|-----|------|-----------------|
| `theme_mode` | string | `system` / `light` / `dark` |
| `onboarding_completed` | boolean | `false` |
| `app_language` | string | `system` → `AppLanguage.fromCode` |

Delete-all-user-data clears **mood/journal Room tables only** — does not reset theme, onboarding, or app language.

### Room schema v2

- `mood_entries`: includes `emotions_json`, `triggers_json` (migration v1→v2)
- `journal_entries`: unchanged from v1
- `MIGRATION_1_2` in `data/local/database/Migrations.kt`
- **No** `fallbackToDestructiveMigration`

### Export payload

JSON via Settings `CreateDocument`; stable English keys; includes emotions/triggers when present.

---

## 6. Domain layer

**Models:** `MoodEntry`, `MoodLevel`, `AppSettings`, `AppThemeMode`, `AppLanguage`, `CompanionLanguage`, `Practice`, `Insight`, `StreakInfo`, emotion/trigger enums, `WellbeingProgress`, etc.

**Settings use cases:** `ObserveAppSettingsUseCase`, `SetThemeModeUseCase`, `SetAppLanguageUseCase`, `BuildExportDataUseCase`, `DeleteAllUserDataUseCase`

**Repositories (interfaces):** `MoodRepository`, `JournalRepository`, `PracticeRepository`, `SettingsRepository`

---

## 7. Presentation layer

**Screens:** Onboarding, Home, MoodLog, Practices, PracticeDetail, Insights, CompanionChat, Settings

**ViewModels:** `RootViewModel` (theme + language + onboarding), `HomeViewModel`, `MoodLogViewModel`, `InsightsViewModel`, `CompanionViewModel`, `SettingsViewModel`, etc.

**Localization:** `presentation/localization/LocalizedContext.kt`; `CompositionLocalProvider(LocalContext)` + `key(appLanguage)` in `ElyriaApp.kt`

**UiText:** `core/base/UiText.kt` — ViewModels emit `StringResource` / `DynamicString`; Composables resolve with `asString(context)`

---

## 8. AI / Companion

- **Reflection:** `GenerateCompanionResponseUseCase` → `MockAICompanion.generateReflection`
- **Chat:** `GenerateCompanionChatResponseUseCase` + in-memory session in `CompanionViewModel`
- **Language:** `MlKitLanguageIdentifier` → `CompanionLanguage`
- **Patterns:** mood pattern summary passed into companion context

---

## 9. Mood coach features

- Fixed emotion vocabulary (12) and trigger tags (12)
- `GetMoodPatternSummaryUseCase` for Insights patterns card
- Inner Garden on Home (`WellbeingProgress`)

---

## 10. Settings / privacy

- Theme: System / Light / Dark
- App language: System / EN / RU / UK / RO
- Export: `application/json` via SAF
- Delete all: mood + journal only
- **No** `INTERNET`, **no** storage permissions in manifest

---

## 11. Localization status

| File | Locale |
|------|--------|
| `res/values/strings.xml` | English (default) |
| `res/values-ru/strings.xml` | Russian |
| `res/values-uk/strings.xml` | Ukrainian |
| `res/values-ro/strings.xml` | Romanian |

- **129** string keys synchronized (`StringResourceKeysTest`)
- Practice **titles/descriptions** in `PracticeCatalog` remain English (content catalog, not UI chrome)

### Hardcoded visible strings (audit)

| Location | Reason |
|----------|--------|
| `data/ai/MockAICompanion.kt` | Generated companion/reflection **content** (multilingual by design, not UI labels) |
| `data/local/practice/PracticeCatalog.kt` | Practice content titles/descriptions (not yet localized catalog) |
| Navigation route checks in `NavGraph.kt` | Internal route matching, not user-visible |

**UI Composables / ViewModels:** no user-visible hardcoded English for chrome; labels use `stringResource` or `UiText`.

---

## 12. Testing status

**Unit tests (debug):** 77 test methods, 0 failures (includes new `AppLanguageTest`, `SetAppLanguageUseCaseTest`, `StringResourceKeysTest`)

**Build:** `cd android && ./gradlew test assembleDebug` — SUCCESS

**Gaps:** `CompanionViewModelTest`, instrumented ML Kit tests, DataStore integration test for language persistence on delete

---

## 13. Known technical debt

- Crisis resources screen placeholder
- WorkManager reminders not implemented
- Practice completion not persisted to Room
- Practice catalog strings not in resources
- PDF export / Health Connect not started

---

## 14. Pending roadmap

- Step 6.3 — Crisis resources screen
- Step 6.4 — WorkManager reminders
- Practice completion persistence
- Step 8 — PDF reports
- Optional Health Connect
- CompanionViewModel unit tests

---

## 15. How to build and verify

```bash
cd android
./gradlew test assembleDebug
```

**Manual checklist (language):**

1. Settings → Language → Russian → UI updates without restart  
2. Kill app → language persists  
3. Delete all data → theme + language + onboarding remain  
4. Companion: UI RU, message RO → reply in Romanian  
5. Export JSON → keys remain English  

---

*Report generated after Step 6.L implementation.*
