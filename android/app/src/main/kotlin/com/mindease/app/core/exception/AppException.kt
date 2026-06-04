package com.mindease.app.core.exception

sealed class AppException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Database(message: String, cause: Throwable? = null) : AppException(message, cause)
    class Validation(message: String) : AppException(message)
    class NotFound(message: String) : AppException(message)
}
