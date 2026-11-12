package com.universall

import com.universall.core.UniPM
import com.universall.core.model.config.AppConfig
import com.universall.main.cli.CommandLineParser
import com.universall.main.config.JsonAppConfigLoader
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.system.exitProcess

fun main(args: Array<String>) = handleErrors {
    val uniPM = UniPM(config = loadConfig(args = args))
    uniPM.run(output = ::println)
}

private fun loadConfig(args: Array<String>): AppConfig {
    val commandLineParser = CommandLineParser()
    val options = commandLineParser.parse(arguments = args)

    val path = Path.of(options.configPath ?: "config.json")
    val configLoader = JsonAppConfigLoader(path = path, json = Json)

    val config = configLoader.loadConfig()
    return config.copy(overwriteOutput = options.overwriteOutput || config.overwriteOutput)
}

private fun handleErrors(action: () -> Unit) {
    try {
        action()
    } catch (exception: IllegalArgumentException) {
        System.err.println("Error: ${exception.message}")
        exitProcess(status = 1)
    }
}
