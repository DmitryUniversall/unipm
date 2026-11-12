package com.universall.core.api.model.config

data class AppConfig(
    val testMode: Boolean,
    val outputFile: String,
    val asciiTree: Boolean,
    val maxDepth: Int,
    val excludeSubstring: String,
    val overwriteOutput: Boolean = false,
    val loadOrder: Boolean = false,
)
