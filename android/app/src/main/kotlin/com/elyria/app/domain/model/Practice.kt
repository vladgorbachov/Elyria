package com.elyria.app.domain.model

data class Practice(
    val id: String,
    val title: String,
    val description: String,
    val durationMinutes: Int,
    val lottieAsset: String,
)
