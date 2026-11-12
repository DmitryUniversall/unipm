package com.universall.main.config

import com.universall.common.config.AppConfigLoader
import com.universall.core.model.config.AppConfig
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class JsonAppConfigLoader(
    private val path: Path,
    private val json: Json,
) : AppConfigLoader {
    override fun loadConfig(): AppConfig {
        val text = try {
            Files.readString(path)
        } catch (exception: IOException) {
            throw IllegalArgumentException("Cannot read configuration '$path': ${exception.message}", exception)
        }

        return try {
            json.decodeFromString<JsonAppConfigDocument>(string = text).toAppConfig()
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid configuration '$path': ${exception.message}", exception)
        }
    }
}
