# MindEase Android — описание приложения

**Версия документа:** 1.2  
**Версия приложения:** 1.0.0 (versionCode 1)  
**Application ID:** `com.mindease.app`  
**Расположение:** корень репозитория — `MindEase.md`  
**Статус разработки:** MVP — шаги 0–2 завершены (Room + репозитории). **Готово к шагу 3** (UseCases: LogMood, Insights, Streak).

---

## Содержание

1. [Назначение и принципы](#1-назначение-и-принципы)
2. [Дизайн и UX](#2-дизайн-и-ux)
3. [Архитектура](#3-архитектура)
4. [Функциональность](#4-функциональность)
5. [Структура кода](#5-структура-кода)
6. [Описание слоёв и ключевых модулей](#6-описание-слоёв-и-ключевых-модулей)
7. [Навигация и потоки экранов](#7-навигация-и-потоки-экранов)
8. [Настройки, сборка и окружение](#8-настройки-сборка-и-окружение)
9. [Зависимости и технологический стек](#9-зависимости-и-технологический-стек)
10. [Тестирование и результаты сборки](#10-тестирование-и-результаты-сборки)
11. [Roadmap MVP](#11-roadmap-mvp)
12. [Безопасность и compliance](#12-безопасность-и-compliance)

---

## 1. Назначение и принципы

**MindEase** — мобильное Android-приложение для ментального благополучия: ежедневный лог настроения, короткие практики осознанности, локальные инсайты и on-device AI-компаньон (без отправки данных на сервер в MVP).

### Ключевые принципы

| Принцип | Реализация |
|--------|------------|
| **Privacy-first** | Нет `INTERNET` в манифесте; данные на устройстве |
| **Не замена терапии** | `DisclaimerBanner` на onboarding, home, settings |
| **Calming UI** | Много воздуха, мягкие формы, приглушённые тени |
| **Accessibility** | TalkBack labels на mood-кругах, touch targets ≥ 48 dp |
| **Clean Architecture** | domain ← data, presentation → domain |

Эталон **масштаба UI** (отступы, размеры шрифтов, touch targets) взят из production-приложения [Fluxly / moneymorpheus](file:///home/kali-user/Development/moneymorpheus) — горизонтальные поля 18–24 dp, заголовки 22–35 sp, минимальная зона нажатия 48 dp.

---

## 2. Дизайн и UX

### 2.1. Стиль интерфейса

- **Стиль:** минималистичный calming UI.
- **Воздух:** padding 16–24 dp (`presentation/ui/theme/Dimens.kt`).
- **Скругления:** 12–16 dp (`radiusSm` / `radiusMd` / `radiusLg`).
- **Тени:** `elevationSubtle = 2.dp` на карточках практик.
- **Анимации (план):** fade + slide, 200–300 ms (`core/constants/AnimationDurations.kt`).
- **Цель UX:** снижение тревоги — крупные цели касания, понятные эмодзи, без визуального шума.

### 2.2. Цветовая палитра

Определена в `presentation/ui/theme/ColorPalette.kt`, акценты настроения — `core/constants/MoodColors.kt`. Material3 `ColorScheme` в `presentation/ui/theme/Theme.kt`. Dynamic Material You **отключён**.

| Token | Light | Dark | Смысл |
|-------|-------|------|--------|
| Primary | `#5B8DEE` | `#4A6FA5` | Спокойный синий — доверие |
| Accent / Growth | `#7ED321` | `#7ED321` | Мягкий зелёный — рост |
| Background | `#F8FAFC` | `#0F172A` | Фон экрана |
| Surface | `#FFFFFF` | `#1E293B` | Карточки |
| Text Primary | `#1E293B` | `#F1F5F9` | Основной текст |
| Text Secondary | `#64748B` | `#94A3B8` | Вторичный текст |
| Mood Lavender | `#C4B5FD` | — | Акцент настроения (низкое) |
| Mood Beige | `#E8DCC8` | — | Позитивные состояния |

### 2.3. Типографика

`TypeScale` + `Typography.kt` (`mindEaseTypography()`), применяется в `Theme.kt`:

| Стиль | Размер | Использование |
|-------|--------|----------------|
| displayLarge | 35 sp | Заголовок onboarding («MindEase») |
| headlineMedium | 22 sp | Заголовки экранов |
| titleMedium | 19 sp | Подзаголовки, streak |
| bodyLarge | 17 sp | Основной текст |
| labelLarge | 15 sp | Метки, длительность практик |
| bodySmall | 13 sp | Disclaimer, подписи |

Шрифт: `FontFamily.SansSerif` (системный, без кастомного TTF в MVP).

### 2.4. Размеры и отступы (`Dimens`)

| Token | Значение | Источник / назначение |
|-------|----------|------------------------|
| screenHorizontal | 20 dp | Поля экрана (Fluxly ~20) |
| sheetPadding | 24 dp | Onboarding, модалки |
| cardPadding | 16 dp | Внутри карточек |
| spacingSm–Xl | 8–24 dp | Вертикальный ритм |
| radiusSm–Lg | 12–16 dp | Карточки, баннеры |
| minTouchTarget | 48 dp | Accessibility |
| moodCircleSize | 56 dp | Mood selector |
| elevationSubtle | 2 dp | Card elevation |

### 2.5. UI-компоненты (реализовано / план)

| Компонент | Файл | Статус |
|-----------|------|--------|
| Mood selector (LazyRow, эмодзи) | `MoodSelector.kt` | ✅ |
| Disclaimer banner | `DisclaimerBanner.kt` | ✅ |
| Bottom navigation (5 вкладок) | `MindEaseScaffold.kt` | ✅ |
| Practice cards | `PracticesScreen.kt` + `PracticesViewModel` | ✅ (каталог через `GetPracticesUseCase`) |
| Progress ring / streak badge | — | 🔲 Шаг 4–6 |
| Lottie на карточках | dependency есть | 🔲 Шаг 4 |
| Insights chart (Canvas) | `InsightsScreen.kt` | 🔲 заглушка |
| Crisis resources screen | — | 🔲 Шаг 6 |

### 2.6. Тема приложения

`AppThemeMode`: `SYSTEM` | `LIGHT` | `DARK` — DataStore → `ObserveAppSettingsUseCase` → `RootViewModel` → `MindEaseApp` → `MindEaseTheme(themeMode)`.

---

## 3. Архитектура

### 3.1. Паттерн

**Clean Architecture + MVVM + Repository + UseCase**

```
┌─────────────────────────────────────────────────────────┐
│  presentation (Compose UI, ViewModel, Navigation)      │
└───────────────────────────┬─────────────────────────────┘
                            │ UseCase / StateFlow
┌───────────────────────────▼─────────────────────────────┐
│  domain (models, repository interfaces, use cases)       │
└───────────────────────────┬─────────────────────────────┘
                            │ interfaces
┌───────────────────────────▼─────────────────────────────┐
│  data (Room, DataStore, AI, repository impl)             │
└─────────────────────────────────────────────────────────┘
         ▲
         │ Hilt (di/)
```

### 3.2. Правила кода

- **UiState:** общий `core/base/UiState.kt` + screen-specific `sealed interface` (`OnboardingUiState`, `MoodLogUiState`, …).
- **UiEvent:** `core/base/UiEvent.kt` для one-shot событий (snackbar, navigation).
- **ViewModel:** наследуют `BaseViewModel`; общаются **только с UseCase**, не с Room/DataStore напрямую.
- **Один UseCase — одна операция** (`CompleteOnboardingUseCase`, `ObserveAppSettingsUseCase`, …).
- **Ранние return**, функции ≤ ~30 строк (целевой лимит).
- **Комментарии:** только WHY и edge cases (на английском в коде).

### 3.3. Асинхронность

- Kotlin Coroutines + `viewModelScope`
- Потоки настроек: `StateFlow` / `Flow` из DataStore
- План: `SharedFlow` для одноразовых событий (snackbar, navigation)

### 3.4. DI (Hilt)

| Module | Назначение |
|--------|------------|
| `AppModule` | `DataStore<Preferences>`, `@IoDispatcher` |
| `RepositoryModule` | Binds: Settings, Mood, Journal, Practice repositories |
| `UseCaseModule` | Binds: `AICompanion` → `MockAICompanion` |
| `DatabaseModule` | ✅ `AppDatabase`, `MoodDao`, `JournalDao` |

**ViewModelModule не нужен:** ViewModels помечены `@HiltViewModel` и создаются Hilt автоматически.

### 3.5. Целевая пакетная структура (соответствие)

Рекомендованная структура Clean Architecture **реализована**; отличия только там, где шаг 2 ещё не подключил Room.

```
com.mindease.app
├── presentation/
│   ├── ui/
│   │   ├── components/     ✅ MoodSelector, DisclaimerBanner, MindEaseScaffold
│   │   ├── screens/        ✅ onboarding, home, mood, practices, insights, settings
│   │   ├── theme/          ✅ ColorPalette, Typography, Dimens, Theme
│   │   └── utils/          ✅ UiExtensions (modifiers)
│   ├── viewmodel/          ✅ + BaseViewModel
│   └── navigation/         ✅ Screen.kt (sealed), NavGraph.kt
├── domain/
│   ├── model/              ✅ MoodEntry, JournalEntry, StreakInfo, Insight, Practice, …
│   ├── usecase/            ✅ onboarding, settings, practice (+ шаг 3: mood, insights)
│   └── repository/         ✅ интерфейсы Mood, Journal, Practice, Settings
├── data/
│   ├── local/
│   │   ├── dao/            ✅ MoodDao, JournalDao (Room)
│   │   ├── entity/         ✅ MoodEntryEntity, JournalEntryEntity
│   │   ├── mapper/         ✅ Entity ↔ Domain
│   │   ├── converter/      ✅ LocalDateTimeConverter (@TypeConverter)
│   │   ├── database/       ✅ AppDatabase v1
│   │   ├── datastore/      ✅ UserPreferences
│   │   └── practice/       ✅ PracticeCatalog (internal)
│   ├── repository/         ✅ Mood/Journal → Room; Practice/Settings — как раньше
│   └── ai/                 ✅ AICompanion, MockAICompanion
├── di/                     ✅ App, Repository, UseCase, Database modules
├── core/
│   ├── base/               ✅ UiState, UiEvent, BaseViewModel
│   ├── constants/          ✅ AppConstants, AnimationDurations, MoodColors
│   ├── utils/              ✅ DateUtils
│   ├── extensions/         ✅ FlowExtensions
│   └── exception/          ✅ AppException
├── MindEaseApplication.kt
└── MainActivity.kt
```

| Рекомендация | Статус |
|--------------|--------|
| presentation → только domain | ✅ ViewModels → UseCase |
| domain не знает data/presentation | ✅ |
| theme в presentation/ui | ✅ перенесено из core/theme |
| Screen sealed + NavGraph | ✅ `Screen.kt`, `NavGraph.kt` |
| BaseViewModel + UiState | ✅ `core/base/` |
| ViewModelModule | ➖ заменён `@HiltViewModel` |

---

## 4. Функциональность

### 4.1. Реализовано (шаги 0–1)

| Функция | Описание |
|---------|----------|
| **Onboarding** | Экран приветствия, disclaimer, кнопка «Get started» → `CompleteOnboardingUseCase` → DataStore |
| **Главная** | Приветствие, placeholder streak (0), mood selector без сохранения |
| **Mood log** | UI + ViewModel; **сохранение в Room** — шаг 3 (`LogMoodUseCase`) |
| **Practices** | Список через `GetPracticesUseCase` → `PracticeRepository` (4 практики MVP) |
| **Insights** | Текстовая заглушка |
| **Settings** | Заглушка + disclaimer |
| **Навигация** | `Screen` sealed interface + `NavGraph`, bottom bar |
| **Тема** | `ObserveAppSettingsUseCase` (переключатель UI — шаг 6) |
| **AI mock** | `MockAICompanion` через `UseCaseModule` (подключение в UI — шаг 5) |

### 4.2. Запланировано (MVP)

| Функция | Шаг |
|---------|-----|
| ~~Сохранение mood / journal в Room~~ | ✅ шаг 2 (DAO + RepositoryImpl) |
| UseCases: LogMood, GetInsights, Streak | 3 |
| Таймер практик, Lottie | 4 |
| On-device AI (mock → ML Kit) | 5 |
| WorkManager reminders, export JSON, delete all | 6 |
| Unit / Compose UI tests | 7 |
| Crisis keywords → resources | 6 |
| Streak recalculation worker | 6 |

### 4.3. Доменные модели

| Модель | Файл | Поля |
|--------|------|------|
| `MoodLevel` | `MoodLevel.kt` | enum VERY_LOW…GREAT (score 1–5) |
| `MoodEntry` | `MoodEntry.kt` | id, moodLevel, note?, loggedAt, sentimentScore? |
| `JournalEntry` | `JournalEntry.kt` | id, text, createdAt |
| `StreakInfo` | `StreakInfo.kt` | currentStreak, longestStreak, lastLogDate? |
| `Insight` | `Insight.kt` | summary, periodStart/End, averageMoodScore? |
| `Practice` | `Practice.kt` | id, title, description, durationMinutes, lottieAsset |
| `AppSettings` | `AppSettings.kt` | themeMode, onboardingCompleted |
| `AppThemeMode` | `AppThemeMode.kt` | SYSTEM, LIGHT, DARK |

---

## 5. Структура кода

Документация: **`MindEase.md`** (корень репозитория). Код: **`android/`**.

```
MindEase/
├── MindEase.md                 ← этот файл
├── .gitignore
└── android/
    ├── app/src/main/kotlin/com/mindease/app/
    │   ├── MindEaseApplication.kt
    │   ├── MainActivity.kt
    │   ├── core/               # base, constants, utils, extensions, exception
    │   ├── di/                 # App, Repository, UseCase, Database
    │   ├── domain/             # model, repository, usecase/*
    │   ├── data/               # local/*, repository, ai
    │   └── presentation/       # navigation, viewmodel, ui/*
    ├── build.gradle.kts
    └── gradlew
```

**Статистика (2026-06-03, после рефакторинга):**

- Kotlin-файлов в `src/main`: **~66**
- Debug APK: **~24 MB** (`android/app/build/outputs/apk/debug/app-debug.apk`)

---

## 6. Описание слоёв и ключевых модулей

### 6.1. Точка входа

| Класс | Роль |
|-------|------|
| `MindEaseApplication` | `@HiltAndroidApp` |
| `MainActivity` | `enableEdgeToEdge()`, `setContent { MindEaseApp() }` |
| `MindEaseApp` | `RootViewModel` → loading / `MindEaseNavGraph` + `MindEaseTheme` |

### 6.2. Presentation — ViewModels

| ViewModel | Базовый класс | UseCase / состояние |
|-----------|---------------|---------------------|
| `RootViewModel` | `BaseViewModel` | `ObserveAppSettingsUseCase` → `RootUiState` |
| `OnboardingViewModel` | `BaseViewModel` | `CompleteOnboardingUseCase` → `OnboardingUiState` |
| `HomeViewModel` | `BaseViewModel` | `HomeUiState` + `StreakInfo` placeholder |
| `MoodLogViewModel` | `BaseViewModel` | `MoodLogUiState.Editing` (сохранение — шаг 3) |
| `PracticesViewModel` | `BaseViewModel` | `GetPracticesUseCase` → `PracticesUiState` |

Все ViewModel наследуют `core/base/BaseViewModel.kt`.

### 6.3. Presentation — UI

| Экран | Composable | ViewModel |
|-------|------------|-----------|
| Onboarding | `OnboardingScreen` | `OnboardingViewModel` |
| Home | `HomeScreen` | `HomeViewModel` |
| Mood | `MoodLogScreen` | `MoodLogViewModel` |
| Practices | `PracticesScreen` | `PracticesViewModel` |
| Insights | `InsightsScreen` | — |
| Settings | `SettingsScreen` | — |

**`MoodSelector`:** горизонтальный `LazyRow`, 5 кругов с эмодзи, `contentDescription` для TalkBack, `Role.Button`, флаг `selected`.

### 6.4. Domain

| Артефакт | Статус |
|----------|--------|
| `SettingsRepository`, `PracticeRepository`, `MoodRepository`, `JournalRepository` | ✅ интерфейсы |
| `CompleteOnboardingUseCase`, `ObserveAppSettingsUseCase`, `GetPracticesUseCase` | ✅ |
| `LogMoodUseCase`, `GetInsightsUseCase`, `GenerateCompanionResponseUseCase` | 🔲 шаг 3–5 |

### 6.5. Data

| Компонент | Статус |
|-----------|--------|
| `UserPreferences` (DataStore) | ✅ keys: `theme_mode`, `onboarding_completed` |
| `SettingsRepositoryImpl` | ✅ |
| **Room** | ✅ `mindease.db`, таблицы `mood_entries`, `journal_entries` |
| `MoodRepositoryImpl` / `JournalRepositoryImpl` | ✅ Room + mappers + `Dispatchers.IO` |
| `StreakInfo` | вычисляется из mood-логов (шаг 3), отдельной таблицы нет |
| **DataStore** | настройки (`theme`, `onboarding`) — без миграции в Room |
| `MockAICompanion` | ✅ rule-based ответы (без UI) |

**Room schema (v1):**

| Таблица | Поля |
|---------|------|
| `mood_entries` | id, mood_level, note, logged_at (ms), sentiment_score |
| `journal_entries` | id, text, created_at (ms) |

**DAO:** `observeAll`, `observeByDateRange`, `getLatest`, `insert`, `deleteAll` (возвращает `Int`).

**DataStore keys:**

| Key | Тип | Значения |
|-----|-----|----------|
| `theme_mode` | String | `system`, `light`, `dark` |
| `onboarding_completed` | Boolean | default `false` |

Имя файла: `mindease_preferences` (`AppConstants.DATASTORE_NAME`).

### 6.6. Navigation (typed routes)

`presentation/navigation/Screen.kt` — **sealed interface** с `@Serializable` destinations:

- `Screen.Onboarding`, `Screen.Main`
- `Screen.Home`, `Screen.Mood`, `Screen.Practices`, `Screen.Insights`, `Screen.Settings`

`NavGraph.kt` (`MindEaseNavGraph`): внешний NavHost (onboarding → main), внутри — tab NavHost + `MindEaseScaffold` + `popUpTo(Screen.Home) { saveState = true }`.

---

## 7. Навигация и потоки экранов

```mermaid
flowchart TD
    Start([Запуск]) --> Root{RootViewModel}
    Root -->|!onboardingCompleted| Onb[OnboardingScreen]
    Root -->|onboardingCompleted| Main[Screen.Main]
    Onb -->|CompleteOnboarding| Main
    Main --> Home[Home]
    Main --> Mood[Mood]
    Main --> Prac[Practices]
    Main --> Ins[Insights]
    Main --> Set[Settings]
```

**Первый запуск:** `onboarding_completed = false` → `Screen.Onboarding`.  
**Повторный запуск:** DataStore `true` → `Screen.Main` → `Screen.Home`.

---

## 8. Настройки, сборка и окружение

### 8.1. Требования

| Параметр | Значение |
|----------|----------|
| minSdk | 24 |
| targetSdk / compileSdk | 35 |
| JVM (compile) | 17 |
| Gradle JDK | **21** (обязательно; JDK 25 не поддерживается AGP) |
| Gradle | 8.14 |

### 8.2. `local.properties` (не коммитить)

Создать в `android/local.properties`:

```properties
sdk.dir=/path/to/Android/sdk
# Опционально для будущего backend-proxy (MVP пустой):
# LLM_API_KEY=
```

`LLM_API_KEY` пробрасывается в `BuildConfig` (MVP всегда пустая строка).

### 8.3. `gradle.properties`

```properties
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.java.home=/usr/lib/jvm/java-21-openjdk-amd64
android.useAndroidX=true
```

На другой машине измените `org.gradle.java.home` или удалите строку и задайте `JAVA_HOME` на JDK 17/21.

### 8.4. Команды сборки

```bash
cd android
./gradlew assembleDebug    # APK debug
./gradlew assembleRelease  # release (minify off)
./gradlew test             # unit tests (см. §10)
```

### 8.5. `.gitignore`

- Корень репо: `android/local.properties`, `android/**/build/`
- `android/.gitignore`: `local.properties`, `secrets.properties`, `*.env`, `build/`

### 8.6. AndroidManifest (важное)

- `android:allowBackup="false"` — снижение утечки бэкапов
- **Нет** `INTERNET` permission — privacy-first MVP
- `windowSoftInputMode="adjustResize"` — клавиатура на Mood log

---

## 9. Зависимости и технологический стек

| Категория | Версия / артефакт |
|-----------|------------------|
| Kotlin | 2.2.20 |
| AGP | 8.11.1 |
| Compose BOM | 2025.02.00 |
| Material3 | via BOM |
| Navigation Compose | 2.8.9 |
| Hilt | 2.57.2 |
| Room | 2.7.1 (KSP; совместимость с Kotlin 2.2) |
| Desugaring | `desugar_jdk_libs` (java.time на minSdk 24) |
| DataStore Preferences | 1.1.2 |
| WorkManager | 2.10.0 |
| Lottie Compose | 6.6.2 |
| ML Kit Language ID | 17.0.6 (заготовка) |
| Serialization | kotlinx-json 1.8.0 |
| Test | JUnit 4.13.2, MockK, Turbine, Compose UI Test |

---

## 10. Тестирование и результаты сборки

### 10.1. Последний прогон (2026-06-03, шаг 2 — Room)

Окружение: Linux, JDK 21 через `gradle.properties`, Android SDK `ANDROID_HOME`.

| Задача | Команда | Результат |
|--------|---------|-----------|
| Unit tests | `./gradlew test` | **BUILD SUCCESSFUL** |
| Debug APK | `./gradlew assembleDebug` | **BUILD SUCCESSFUL** |
| Комбинированно | `./gradlew test assembleDebug` | **BUILD SUCCESSFUL** (74 tasks, UP-TO-DATE) |

### 10.2. Unit / instrumented tests

| Тип | Статус | Примечание |
|-----|--------|------------|
| `MoodEntryMapperTest` | ✅ 3 теста | round-trip, mood level, unknown → NEUTRAL |
| `JournalEntryMapperTest` | ✅ 2 теста | round-trip, timestamp |
| `src/androidTest` (Compose UI) | **Нет** | шаг 7 |

**Итог:** `./gradlew test` — **BUILD SUCCESSFUL** (5 unit-тестов мапперов).

### 10.3. Рекомендуемые тесты (шаг 7)

| Класс | Тип | Что проверять |
|-------|-----|----------------|
| `CompleteOnboardingUseCase` | Unit + MockK | вызов `setOnboardingCompleted(true)` |
| `LogMoodUseCase` | Unit | 🔲 после Room |
| `MoodSelector` | Compose UI | выбор mood, semantics selected |
| `UpdateStreakUseCase` | Unit | пропуск дня, сброс streak |

### 10.4. Известные ограничения сборки

- **JDK 25:** Gradle падает с сообщением `25.0.3` — использовать JDK 21 или 17.
- **Release signing:** пока debug keys (`signingConfig` не настроен для release).
- **ProGuard:** `isMinifyEnabled = false` в release.

---

## 11. Roadmap MVP

| Неделя | Шаг | Содержание | Статус |
|--------|-----|------------|--------|
| 1 | 0 | Gradle, Hilt, зависимости | ✅ |
| 1 | 1 | Структура CA, theme, nav, DataStore | ✅ |
| 1–2 | 2 | Room + repositories | ✅ |
| 2 | 3 | UseCases + сохранение mood | 🔲 **следующий** |
| 2–3 | 4 | UI practices, timer, Lottie | 🔲 |
| 3–4 | 5 | AI mock / ML Kit | 🔲 |
| 4–5 | 6 | WorkManager, export, crisis | 🔲 |
| 6 | 7 | Tests, ProGuard, signing | 🔲 |

**Критерии готовности MVP:** onboarding + daily mood + 4–5 practices + insights + streak + local-only + reminders + export/delete + a11y + disclaimer.

---

## 12. Безопасность и compliance

| Требование | Статус MVP |
|------------|------------|
| Данные только on-device | ✅ нет сетевых permission |
| Disclaimer | ✅ строка `disclaimer_short` |
| GDPR / CCPA delete & export | 🔲 Settings (шаг 6) |
| Crisis keywords → help resources | 🔲 `core/crisis/` (шаг 6) |
| Секреты не в git | ✅ `.gitignore` |
| Keystore для будущего API | 🔲 backend-proxy only |

---

## Связанные документы

- План разработки (чат / шаги 0–8) — исходное ТЗ проекта.
- Эталон UI-масштаба: `/home/kali-user/Development/moneymorpheus/lib/core/constants.dart`, `MindEaseDimens.kt`.

---

*Обновлять при завершении шагов 2–7 и появлении unit/UI тестов.*
