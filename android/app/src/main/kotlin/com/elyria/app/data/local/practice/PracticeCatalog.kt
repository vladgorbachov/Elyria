package com.elyria.app.data.local.practice

import com.elyria.app.domain.model.Practice

internal object PracticeCatalog {
    val all: List<Practice> = listOf(
        Practice("breathing", "Breathing", "Calm breath cycles", 5, "practice_calm.json"),
        Practice("grounding", "Grounding", "5-4-3-2-1 senses", 7, "practice_calm.json"),
        Practice("gratitude", "Gratitude", "Three good things", 5, "practice_calm.json"),
        Practice("body_scan", "Body scan", "Gentle body awareness", 10, "practice_calm.json"),
    )
}
