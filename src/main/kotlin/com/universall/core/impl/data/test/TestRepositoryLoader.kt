package com.universall.core.impl.data.test

import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

internal class TestRepositoryLoader(
    private val json: Json,
) {
    fun load(path: Path): Map<String, List<String>> {
        val text = try {
            Files.readString(path)
        } catch (exception: IOException) {
            throw IllegalArgumentException(
                "Cannot read test repository '$path': ${exception.message}",
                exception,
            )
        }

        return try {
            json.decodeFromString<Map<String, List<String>>>(string = text)
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException(
                "Invalid test repository '$path': ${exception.message}",
                exception,
            )
        }
    }
}
