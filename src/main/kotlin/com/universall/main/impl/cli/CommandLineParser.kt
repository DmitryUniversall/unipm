package com.universall.main.impl.cli

import com.universall.common.api.cli.LaunchOption
import com.universall.common.api.cli.LaunchOptionKey
import com.universall.common.api.cli.LaunchOptionsParser
import com.universall.main.impl.model.cli.LaunchOptions

class CommandLineParser {
    fun parse(arguments: Array<String>): LaunchOptions {
        val values = parser.parse(arguments = arguments)

        return LaunchOptions(
            configPath = values[configKey],
            overwriteOutput = values[overwriteOutputKey] ?: false,
        )
    }

    private companion object {
        val configKey = LaunchOptionKey<String>(name = "--config")
        val overwriteOutputKey = LaunchOptionKey<Boolean>(name = "--overwrite-output")

        val parser = LaunchOptionsParser(
            options = listOf(
                LaunchOption.Parameter(key = configKey) { value -> value },
                LaunchOption.Flag(key = overwriteOutputKey),
            ),
        )
    }
}
