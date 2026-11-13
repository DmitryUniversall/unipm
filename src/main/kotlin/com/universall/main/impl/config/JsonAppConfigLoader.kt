package com.universall.main.impl.config

import com.universall.common.api.config.ConfigLoader
import com.universall.core.api.model.config.AppConfig
import kotlinx.serialization.json.Json
import java.nio.file.Path

internal class JsonAppConfigLoader(
    path: Path,
    json: Json,
) : ConfigLoader<AppConfig>(
    path = path,
    parseConfig = { text ->
        json.decodeFromString<JsonAppConfigDocument>(string = text).toAppConfig()
    }
)
