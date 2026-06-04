package com.elyria.app.data.ai

import com.elyria.app.domain.ai.AICompanion
import com.elyria.app.domain.model.CompanionChatContext
import com.elyria.app.domain.model.CompanionContext
import com.elyria.app.domain.model.CompanionLanguage
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodLevel
import com.elyria.app.domain.model.MoodTrend
import com.elyria.app.domain.model.MoodTrigger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAICompanion @Inject constructor() : AICompanion {

    override suspend fun generateReflection(context: CompanionContext): String {
        if (context.entryCount == 0) {
            return "Once you add a few mood check-ins, Elyria will reflect on your recent patterns here."
        }

        return when {
            context.averageMoodScore <= 2.0 -> lowAverageReflection(context.trend)
            context.averageMoodScore >= 4.0 -> highAverageReflection(context.trend)
            context.trend == MoodTrend.UP -> UPWARD_REFLECTION
            context.trend == MoodTrend.DOWN -> DOWNWARD_REFLECTION
            else -> STABLE_REFLECTION
        }
    }

    override suspend fun generateChatResponse(
        userMessage: String,
        context: CompanionChatContext,
        language: CompanionLanguage,
    ): String {
        val phrases = phrasesFor(language)
        val message = userMessage.trim().lowercase()
        if (message.isBlank()) {
            return phrases.blankPrompt
        }
        if (containsAny(message, CRISIS_KEYWORDS)) {
            return phrases.crisis
        }
        patternContextResponse(context, phrases)?.let { return it }
        if (containsAny(message, STRESS_KEYWORDS) || containsAny(message, LOW_MOOD_KEYWORDS)) {
            return phrases.stress
        }
        if (context.latestMood == MoodLevel.VERY_LOW || context.latestMood == MoodLevel.LOW) {
            return phrases.lowMoodContext
        }
        if (context.trend == MoodTrend.DOWN) {
            return phrases.trendDown
        }
        if (context.trend == MoodTrend.UP) {
            return phrases.trendUp
        }
        if (containsAny(message, PRACTICE_QUESTION_KEYWORDS)) {
            return phrases.practiceSuggestion
        }
        return phrases.generic
    }

    private fun phrasesFor(language: CompanionLanguage): ChatPhrases {
        return PHRASES[language] ?: PHRASES.getValue(CompanionLanguage.ENGLISH)
    }

    private fun containsAny(text: String, keywords: List<String>): Boolean {
        return keywords.any { text.contains(it) }
    }

    private fun patternContextResponse(
        context: CompanionChatContext,
        phrases: ChatPhrases,
    ): String? {
        if (context.topTriggers.contains(MoodTrigger.SLEEP)) {
            return phrases.sleepPattern
        }
        if (context.topEmotions.contains(EmotionCategory.OVERWHELMED)) {
            return phrases.overwhelmedPattern
        }
        if (context.topEmotions.any { it == EmotionCategory.GRATEFUL || it == EmotionCategory.HOPEFUL }) {
            return phrases.positiveEmotionPattern
        }
        return null
    }

    private fun lowAverageReflection(trend: MoodTrend): String {
        return when (trend) {
            MoodTrend.UP ->
                "This period still feels emotionally heavy in places, even with some upward movement. " +
                    "A short grounding exercise may help, and reaching out to someone you trust can help too."
            MoodTrend.DOWN, MoodTrend.STABLE ->
                "This period looks emotionally heavier than usual. " +
                    "Try a gentle grounding exercise, and consider reaching out to someone you trust if you need support."
        }
    }

    private fun highAverageReflection(trend: MoodTrend): String {
        return when (trend) {
            MoodTrend.DOWN ->
                "Parts of this period still look positive overall. " +
                    "Notice what helped on the brighter days and keep one small supportive habit going."
            MoodTrend.UP, MoodTrend.STABLE ->
                "This period looks relatively positive. " +
                    "It may be a good moment to notice what helped and keep one small supportive habit going."
        }
    }

    private data class ChatPhrases(
        val blankPrompt: String,
        val stress: String,
        val lowMoodContext: String,
        val trendDown: String,
        val trendUp: String,
        val practiceSuggestion: String,
        val generic: String,
        val crisis: String,
        val sleepPattern: String,
        val overwhelmedPattern: String,
        val positiveEmotionPattern: String,
    )

    private companion object {
        val LOW_MOOD_KEYWORDS = listOf(
            "sad", "down", "empty", "lonely", "tired", "exhausted", "bad",
            "груст", "плох", "устал", "устала", "пуст", "одинок", "тяжел",
            "сумн", "важк", "втом", "самотн", "важко",
            "trist", "obosit", "obosită", "singur",
        )
        val STRESS_KEYWORDS = listOf(
            "stress", "stressed", "anxious", "panic", "overwhelmed", "worried",
            "тревог", "стресс", "паник", "перегруз",
            "тривог", "стрес", "панік",
            "anxiet", "stres", "panic", "îngrijor",
        )
        val CRISIS_KEYWORDS = listOf(
            "suicide", "kill myself", "self harm", "hurt myself", "end it",
            "суицид", "покончить", "самоповреж", "причинить себе вред",
            "самогуб", "покінчити", "самопошкод",
            "sinucid", "mă sinucid", "mă rănesc",
        )
        val PRACTICE_QUESTION_KEYWORDS = listOf(
            "what should i do", "what can i do",
            "что мне делать", "что делать",
            "що мені робити", "що робити",
            "ce ar trebui", "ce pot face",
        )

        const val UPWARD_REFLECTION =
            "Your mood seems to be improving over this period. " +
                "You might keep supporting that rhythm with a short gratitude or breathing practice."
        const val DOWNWARD_REFLECTION =
            "Your mood looks a little lower across this period. " +
                "A short grounding practice may help you slow down and reconnect with the present moment."
        const val STABLE_REFLECTION =
            "Your mood looks mostly stable across this period. " +
                "A brief breathing practice can help you keep that steady rhythm."

        val PHRASES = mapOf(
            CompanionLanguage.ENGLISH to ChatPhrases(
                blankPrompt =
                    "You can write a few words about how you feel, and Elyria will reflect gently with you.",
                stress =
                    "That sounds like a heavy moment. Try one slow breath and one small grounding action.",
                lowMoodContext =
                    "Thank you for sharing that. A gentle grounding exercise or a few calm breaths may help you feel a little more settled.",
                trendDown =
                    "Your recent check-ins suggest a softer stretch lately. One small supportive step today can be enough.",
                trendUp =
                    "Your recent pattern looks a little brighter. You might keep supporting that rhythm with a short gratitude or breathing practice.",
                practiceSuggestion =
                    "You could try one small practice — a few minutes of breathing, grounding, or noting one thing you appreciate.",
                generic =
                    "Thank you for sharing. Take a moment to notice what you need right now, even if it is only rest.",
                crisis =
                    "I'm sorry you're feeling this way. Elyria is not emergency support, but you deserve " +
                        "immediate help from a trusted person or local emergency service if you might be in danger.",
                sleepPattern =
                    "Sleep seems to appear in your recent check-ins. A short evening routine might be worth trying.",
                overwhelmedPattern =
                    "A small grounding step may be more useful than a big plan right now.",
                positiveEmotionPattern =
                    "It may help to note what supported that feeling.",
            ),
            CompanionLanguage.RUSSIAN to ChatPhrases(
                blankPrompt =
                    "Напишите несколько слов о том, как вы себя чувствуете, и Elyria мягко отразит это вместе с вами.",
                stress =
                    "Похоже, сейчас момент ощущается тяжело. Попробуй один медленный вдох и небольшое заземляющее действие.",
                lowMoodContext =
                    "Спасибо, что поделились. Небольшая заземляющая практика или несколько спокойных вдохов могут немного помочь.",
                trendDown =
                    "Недавние записи показывают более мягкий период. Одного небольшого поддерживающего шага сегодня может быть достаточно.",
                trendUp =
                    "Недавний ритм выглядит чуть светлее. Можно поддержать его короткой практикой благодарности или дыхания.",
                practiceSuggestion =
                    "Можно попробовать одну небольшую практику — несколько минут дыхания, заземления или одну вещь, за которую вы благодарны.",
                generic =
                    "Спасибо, что поделились. Заметьте, что вам нужно прямо сейчас, даже если это только отдых.",
                crisis =
                    "Мне жаль, что вам сейчас так тяжело. Elyria не заменяет экстренную помощь, но вы заслуживаете " +
                        "немедленной поддержки от близкого человека или местной службы экстренной помощи, если есть опасность.",
                sleepPattern =
                    "Сон часто появляется в недавних записях. Короткий вечерний ритуал может быть полезен.",
                overwhelmedPattern =
                    "Небольшой шаг заземления сейчас может быть полезнее большого плана.",
                positiveEmotionPattern =
                    "Может помочь заметить, что поддержало это чувство.",
            ),
            CompanionLanguage.UKRAINIAN to ChatPhrases(
                blankPrompt =
                    "Напишіть кілька слів про те, як ви почуваєтесь, і Elyria м'яко відобразить це разом із вами.",
                stress =
                    "Схоже, зараз момент відчувається важким. Спробуй один повільний вдих і невелику вправу на заземлення.",
                lowMoodContext =
                    "Дякую, що поділилися. Невелика вправа на заземлення або кілька спокійних вдихів можуть трохи допомогти.",
                trendDown =
                    "Останні записи показують м'якший період. Одного невеликого підтримуючого кроку сьогодні може бути достатньо.",
                trendUp =
                    "Останній ритм виглядає трохи світліше. Його можна підтримати короткою практикою вдячності або дихання.",
                practiceSuggestion =
                    "Можна спробувати одну невелику практику — кілька хвилин дихання, заземлення або одну річ, за яку ви вдячні.",
                generic =
                    "Дякую, що поділилися. Помітьте, що вам потрібно зараз, навіть якщо це лише відпочинок.",
                crisis =
                    "Мені шкода, що вам зараз так важко. Elyria не замінює екстрену допомогу, але ви заслуговуєте " +
                        "негайної підтримки від близької людини або місцевої служби екстреної допомоги, якщо є небезпека.",
                sleepPattern =
                    "Сон часто з’являється в останніх записах. Короткий вечірній ритуал може бути вартим спроби.",
                overwhelmedPattern =
                    "Невеликий крок заземлення зараз може бути кориснішим за великий план.",
                positiveEmotionPattern =
                    "Може допомогти помітити, що підтримало це відчуття.",
            ),
            CompanionLanguage.ROMANIAN to ChatPhrases(
                blankPrompt =
                    "Scrie câteva cuvinte despre cum te simți, iar Elyria va reflecta blând împreună cu tine.",
                stress =
                    "Pare că momentul este greu acum. Încearcă o respirație lentă și un mic exercițiu de ancorare.",
                lowMoodContext =
                    "Mulțumesc că ai împărtășit. Un exercițiu blând de ancorare sau câteva respirații calme te pot ajuta puțin.",
                trendDown =
                    "Înregistrările recente sugerează o perioadă mai blândă. Un singur pas mic de sprijin astăzi poate fi suficient.",
                trendUp =
                    "Ritmul recent pare puțin mai luminos. Îl poți susține cu o scurtă practică de recunoștință sau respirație.",
                practiceSuggestion =
                    "Poți încerca o practică mică — câteva minute de respirație, ancorare sau un lucru pentru care ești recunoscător.",
                generic =
                    "Mulțumesc că ai împărtășit. Observă ce ai nevoie acum, chiar dacă este doar odihnă.",
                crisis =
                    "Îmi pare rău că te simți așa. Elyria nu înlocuiește ajutorul de urgență, dar meriți " +
                        "sprijin imediat de la o persoană de încredere sau serviciul local de urgență dacă există pericol.",
                sleepPattern =
                    "Somnul pare să apară des în înregistrările recente. O rutină scurtă de seară ar putea merita încercată.",
                overwhelmedPattern =
                    "Un pas mic de ancorare poate fi mai util decât un plan mare chiar acum.",
                positiveEmotionPattern =
                    "Poate ajuta să notezi ce a susținut acel sentiment.",
            ),
        )
    }
}
