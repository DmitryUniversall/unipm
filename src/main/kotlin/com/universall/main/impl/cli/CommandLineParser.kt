package com.universall.main.impl.cli

import com.universall.common.api.cli.LaunchOption
import com.universall.common.api.cli.LaunchOptionKey
import com.universall.common.api.cli.LaunchOptionsParser
import com.universall.main.impl.model.cli.LaunchOptions

internal class CommandLineParser {
    fun parse(arguments: Array<String>): LaunchOptions {
        val values = parser.parse(arguments = arguments)

        val packageSpec = values[packageKey]
        val packageInfoPath = values[packageInfoKey]
        require(value = (packageSpec != null) != (packageInfoPath != null)) { "Specify exactly one of --package or --package-info" }

        return LaunchOptions(
            configPath = values[configKey],
            overwriteOutput = values[overwriteOutputKey] ?: false,
            packageSpec = packageSpec,
            packageInfoPath = packageInfoPath,
        )
    }

    private companion object {
        val configKey = LaunchOptionKey<String>(name = "--config")
        val overwriteOutputKey = LaunchOptionKey<Boolean>(name = "--overwrite-output")
        val packageKey = LaunchOptionKey<String>(name = "--package")
        val packageInfoKey = LaunchOptionKey<String>(name = "--package-info")

        val parser = LaunchOptionsParser(
            options = listOf(
                LaunchOption.Parameter(key = configKey) { value -> value },
                LaunchOption.Flag(key = overwriteOutputKey),
                LaunchOption.Parameter(key = packageKey) { value -> value },
                LaunchOption.Parameter(key = packageInfoKey) { value -> value },
            ),
        )
    }
}
