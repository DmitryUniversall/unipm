package com.universall.core.impl.domain.graph.format.ascii

import com.universall.core.impl.domain.model.graph.DependencyGraph
import com.universall.core.impl.domain.model.pkg.PackageCoordinates

internal class AsciiTreeFormatter {
    fun format(graph: DependencyGraph): List<String> {
        val lines = mutableListOf<String>()
        val visited = mutableSetOf<PackageCoordinates>()
        val stack = ArrayDeque<TreeEntry>()
        stack.addLast(element = TreeEntry(coordinates = graph.root))

        while (stack.isNotEmpty()) {
            val entry = stack.removeLast()
            val repeated = !visited.add(element = entry.coordinates)
            val suffix = if (repeated) " [already shown]" else ""
            lines.add(element = "${entry.prefix}${entry.branch}${entry.coordinates}$suffix")
            if (repeated) continue

            val prefix = entry.prefix + when (entry.branch) {
                "|-- " -> "|   "
                "`-- " -> "    "
                else -> ""
            }
            val dependencies = graph.dependencies.getValue(key = entry.coordinates)

            for (index in dependencies.indices.reversed()) {
                stack.addLast(
                    element = TreeEntry(
                        coordinates = dependencies[index],
                        prefix = prefix,
                        branch = if (index == dependencies.lastIndex) "`-- " else "|-- ",
                    ),
                )
            }
        }

        return lines
    }
}

private data class TreeEntry(
    val coordinates: PackageCoordinates,
    val prefix: String = "",
    val branch: String = "",
)
