package com.universall.common.api.config

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

open class ConfigLoader<T>(
    private val path: Path,
    private val parseConfig: (String) -> T,
) {
    fun loadConfig(): T {
        val text = try {
            Files.readString(path)
        } catch (exception: IOException) {
            throw IllegalArgumentException("Cannot read configuration '$path': ${exception.message}", exception)
        }

        return try {
            parseConfig(text)
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid configuration '$path': ${exception.message}", exception)
        }
    }
}
