package com.elyria.app.domain.model

data class WellbeingProgress(
    val checkInsThisWeek: Int,
    val practicesThisWeek: Int,
    val reflectionCountThisWeek: Int,
    val gardenLevel: Int,
)
