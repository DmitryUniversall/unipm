package com.universall.main.impl.pkg.json

import com.universall.common.api.config.ConfigLoader
import com.universall.core.api.model.pkg.PackageInfo
import kotlinx.serialization.json.Json
import java.nio.file.Path

internal class JsonPackageInfoLoader(
    path: Path,
    json: Json,
) : ConfigLoader<PackageInfo>(
    path = path,
    parseConfig = { text ->
        json.decodeFromString<JsonPackageInfoDocument>(string = text).toPackageInfo()
    },
)
