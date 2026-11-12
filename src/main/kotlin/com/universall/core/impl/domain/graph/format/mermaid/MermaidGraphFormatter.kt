package com.universall.core.impl.domain.graph.format.mermaid

import com.universall.core.impl.domain.model.graph.DependencyGraph

internal class MermaidGraphFormatter {
    fun format(graph: DependencyGraph): String {
        val identifiers = graph.dependencies.keys.withIndex().associate { it.value to "n${it.index}" }

        return buildString {
            appendLine(value = "flowchart TD")

            for ((coordinates, identifier) in identifiers) {
                appendLine(value = "    $identifier[\"${escape(value = coordinates.toString())}\"]")
            }

            for ((coordinates, dependencies) in graph.dependencies) {
                for (dependency in dependencies) {
                    appendLine(value = "    ${identifiers.getValue(key = coordinates)} --> ${identifiers.getValue(key = dependency)}")
                }
            }
        }
    }

    private fun escape(value: String): String = value
        .replace(oldValue = "#", newValue = "#35;")
        .replace(oldValue = "\"", newValue = "#quot;")
        .replace(oldValue = "&", newValue = "#38;")
        .replace(oldValue = "<", newValue = "#60;")
        .replace(oldValue = ">", newValue = "#62;")
}
