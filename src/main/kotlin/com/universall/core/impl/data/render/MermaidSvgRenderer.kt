package com.universall.core.impl.data.render

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.TimeUnit

internal class MermaidSvgRenderer(
    private val command: List<String>,
) {
    fun render(mermaid: String, path: Path, overwriteOutput: Boolean) {
        try {
            val target = path.toAbsolutePath()
            Files.createDirectories(target.parent)

            val directory = Files.createTempDirectory(target.parent, ".unipm-")
            val svg = directory.resolve("graph.svg")
            val log = directory.resolve("renderer.log")

            try {
                renderSvg(mermaid = mermaid, svg = svg, log = log)

                if (overwriteOutput) {
                    Files.move(svg, target, StandardCopyOption.REPLACE_EXISTING)
                } else {
                    Files.move(svg, target)
                }
            } finally {
                Files.deleteIfExists(svg)
                Files.deleteIfExists(log)
                Files.deleteIfExists(directory)
            }
        } catch (exception: IOException) {
            throw IllegalArgumentException("Cannot save SVG '$path': ${exception.message}", exception)
        } catch (exception: InterruptedException) {
            Thread.currentThread().interrupt()
            throw IllegalArgumentException("SVG rendering was interrupted", exception)
        }
    }

    private fun renderSvg(mermaid: String, svg: Path, log: Path) {
        val process = ProcessBuilder(command + listOf("--input", "-", "--output", svg.toString(), "--quiet"))
            .redirectErrorStream(true)
            .redirectOutput(log.toFile())
            .start()

        try {
            // outputStream for kotlin, input stream for process
            process.outputStream.bufferedWriter(charset = Charsets.UTF_8).use { it.write(mermaid) }
            require(value = process.waitFor(60, TimeUnit.SECONDS)) { "SVG rendering timed out" }
            require(value = process.exitValue() == 0) { "Cannot render SVG: ${Files.readString(log).trim()}" }
        } finally {
            if (process.isAlive) process.destroyForcibly().waitFor()
        }
    }
}
