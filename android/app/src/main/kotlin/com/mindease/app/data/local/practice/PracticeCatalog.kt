package com.mindease.app.data.local.practice

import com.mindease.app.domain.model.Practice

internal object PracticeCatalog {
    val all: List<Practice> = listOf(
        Practice("breathing", "Breathing", "Calm breath cycles", 5, "breathing.json"),
        Practice("grounding", "Grounding", "5-4-3-2-1 senses", 7, "grounding.json"),
        Practice("gratitude", "Gratitude", "Three good things", 5, "gratitude.json"),
        Practice("body_scan", "Body scan", "Gentle body awareness", 10, "body_scan.json"),
    )
}
