package com.universall.main.cli

import com.universall.main.model.cli.LaunchOptions

class CommandLineParser {
    fun parse(arguments: Array<String>): LaunchOptions {
        val suppliedOptions = mutableSetOf<String>()
        val supportedOptions = setOf("--config", "--overwrite-output")
        var configPath: String? = null
        var overwriteOutput = false
        var position = 0

        while (position < arguments.size) {
            val option = arguments[position]

            require(value = option in supportedOptions) { "Unknown option: $option" }
            require(value = option !in suppliedOptions) { "Duplicate option: $option" }
            suppliedOptions += option
            position++

            if (option == "--overwrite-output") {
                overwriteOutput = true
                continue
            }

            require(value = position < arguments.size && !arguments[position].startsWith(prefix = "--")) { "Missing value for $option" }
            val value = arguments[position]
            require(value = value.isNotBlank()) { "Empty value for $option" }
            configPath = value
            position++
        }

        return LaunchOptions(
            configPath = configPath,
            overwriteOutput = overwriteOutput,
        )
    }
}
