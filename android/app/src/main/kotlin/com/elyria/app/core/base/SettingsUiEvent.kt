package com.elyria.app.core.base

data class LaunchExportDocument(
    val fileName: String,
    val json: String,
) : UiEvent
