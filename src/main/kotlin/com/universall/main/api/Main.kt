package com.universall.main.api

import com.universall.core.api.UniPm
import com.universall.core.api.model.config.AppConfig
import com.universall.core.api.model.pkg.PackageInfo
import com.universall.main.impl.cli.CommandLineParser
import com.universall.main.impl.config.JsonAppConfigLoader
import com.universall.main.impl.model.cli.LaunchOptions
import com.universall.main.impl.pkg.json.JsonPackageInfoLoader
import com.universall.main.impl.pkg.cli.PackageInfoCliParser
import kotlinx.serialization.json.Json
import java.net.http.HttpClient
import java.nio.file.Path
import java.time.Duration
import kotlin.system.exitProcess

fun main(args: Array<String>) = handleErrors {
    val commandLineParser = CommandLineParser()
    val options = commandLineParser.parse(arguments = args)

    val config = loadConfig(options = options)
    val packageInfo = loadPackageInfo(options = options)

    createHttpClient().use { httpClient ->
        val uniPM = UniPm.create(
            config = config,
            packageInfo = packageInfo,
            httpClient = httpClient,
            json = Json,
            mermaidCommand = listOf("node", "node_modules/@mermaid-js/mermaid-cli/src/cli.js"),
        )

        uniPM.run(output = ::println)
    }
}

private fun loadConfig(options: LaunchOptions): AppConfig {
    val path = Path.of(options.configPath ?: "other/generic_config.json")
    val configLoader = JsonAppConfigLoader(path = path, json = Json)

    val config = configLoader.loadConfig()
    return config.copy(overwriteOutput = options.overwriteOutput || config.overwriteOutput)
}

private fun loadPackageInfo(options: LaunchOptions): PackageInfo {
    val packageSpec = options.packageSpec
    if (packageSpec != null) {
        return PackageInfoCliParser().parse(value = packageSpec)
    }

    val path = Path.of(requireNotNull(value = options.packageInfoPath) { "Missing --package-info path" })
    return JsonPackageInfoLoader(path = path, json = Json).loadConfig()
}

private fun createHttpClient(): HttpClient = HttpClient.newBuilder()
    .connectTimeout(Duration.ofSeconds(10))
    .followRedirects(HttpClient.Redirect.NORMAL)
    .build()

private fun handleErrors(action: () -> Unit) {
    try {
        action()
    } catch (exception: IllegalArgumentException) {
        System.err.println("Error: ${exception.message}")
        exitProcess(status = 1)
    }
}
