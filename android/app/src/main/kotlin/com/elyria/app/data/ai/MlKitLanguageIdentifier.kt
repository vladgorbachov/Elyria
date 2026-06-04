package com.elyria.app.data.ai

import com.elyria.app.domain.ai.LanguageIdentifier
import com.elyria.app.domain.model.CompanionLanguage
import com.google.android.gms.tasks.Task
import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class MlKitLanguageIdentifier @Inject constructor() : LanguageIdentifier {

    private val client = LanguageIdentification.getClient()

    override suspend fun identifyLanguage(text: String): CompanionLanguage {
        val normalized = text.trim()
        if (normalized.length < MIN_TEXT_LENGTH) {
            return CompanionLanguage.UNKNOWN
        }
        return try {
            val code = client.identifyLanguage(normalized).await()
            CompanionLanguage.fromCode(code)
        } catch (_: Exception) {
            CompanionLanguage.UNKNOWN
        }
    }

    private companion object {
        const val MIN_TEXT_LENGTH = 4
    }
}

private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
    addOnCompleteListener { task ->
        if (continuation.isCancelled) return@addOnCompleteListener
        if (task.isSuccessful) {
            continuation.resume(task.result)
        } else {
            continuation.resumeWithException(
                task.exception ?: IllegalStateException("ML Kit task failed"),
            )
        }
    }
}
