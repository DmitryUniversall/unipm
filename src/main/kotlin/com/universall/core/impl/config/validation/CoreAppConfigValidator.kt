package com.universall.core.impl.config.validation

import com.universall.core.api.model.config.AppConfig
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.LinkOption
import java.nio.file.Path

internal class CoreAppConfigValidator {
    fun validate(config: AppConfig) {
        validateOutputFile(value = config.outputFile, overwriteOutput = config.overwriteOutput)
        validateMaxDepth(value = config.maxDepth)
    }

    private fun validateMaxDepth(value: Int) {
        require(value = value >= 0) { "Field 'max_depth' must be a non-negative integer" }
    }

    private fun validateOutputFile(value: String, overwriteOutput: Boolean) {
        require(value = value.isNotBlank()) { "Field 'output_file' must contain a file path" }
        require(value = value.endsWith(suffix = ".svg", ignoreCase = true)) { "Field 'output_file' must have the .svg extension" }

        val path = try {
            Path.of(value)
        } catch (exception: InvalidPathException) {
            throw IllegalArgumentException("Invalid file path in 'output_file'", exception)
        }

        require(value = !Files.isDirectory(path)) { "Field 'output_file' points to a directory: $path" }
        require(value = overwriteOutput || !Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            "Field 'output_file' already exists: $path. Set 'overwrite_output' to true or pass --overwrite-output to replace it"
        }
    }
}
