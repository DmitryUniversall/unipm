package com.universall.main.impl.model.cli

internal data class LaunchOptions(
    val configPath: String? = null,
    val overwriteOutput: Boolean = false,
    val packageSpec: String? = null,
    val packageInfoPath: String? = null,
)
