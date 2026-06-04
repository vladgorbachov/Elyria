package com.elyria.app.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asResultFlow(): Flow<Result<T>> {
    return map { Result.success(it) }
        .catch { emit(Result.failure(it)) }
}
