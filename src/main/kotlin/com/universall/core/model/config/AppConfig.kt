package com.universall.core.model.config

data class AppConfig(
    val testMode: Boolean,
    val outputFile: String,
    val asciiTree: Boolean,
    val maxDepth: Int,
    val excludeSubstring: String,
    val overwriteOutput: Boolean = false,
)
