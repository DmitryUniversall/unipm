package com.universall.core.impl.domain.graph

import com.universall.core.impl.domain.model.graph.DependencyGraph
import com.universall.core.impl.domain.model.pkg.PackageCoordinates
import com.universall.core.impl.domain.resolver.DependenciesResolver

internal class DependencyGraphBuilder(
    private val resolver: DependenciesResolver,
) {
    fun build(maxDepth: Int, excludeSubstring: String): DependencyGraph {
        val root = resolver.getRoot()
        val graph = linkedMapOf<PackageCoordinates, List<PackageCoordinates>>()
        val cache = mutableMapOf<PackageCoordinates, List<PackageCoordinates>>()
        val depths = mutableMapOf<PackageCoordinates, Int>()
        val stack = ArrayDeque<TraversalStep>()
        stack.addLast(element = TraversalStep(coordinates = root, depth = 0))

        while (stack.isNotEmpty()) {
            val step = stack.removeLast()
            val previousDepth = depths[step.coordinates]
            if (previousDepth != null && previousDepth <= step.depth) continue

            depths[step.coordinates] = step.depth
            graph[step.coordinates] = emptyList()
            if (step.depth == maxDepth) continue

            val dependencies = cache.getOrPut(key = step.coordinates) {
                resolver.resolve(coordinates = step.coordinates)
            }.filter { excludeSubstring.isEmpty() || !it.name.contains(other = excludeSubstring) }

            graph[step.coordinates] = dependencies

            for (dependency in dependencies.asReversed()) {
                stack.addLast(element = TraversalStep(coordinates = dependency, depth = step.depth + 1))
            }
        }

        return DependencyGraph(root = root, dependencies = graph)
    }
}

private data class TraversalStep(
    val coordinates: PackageCoordinates,
    val depth: Int,
)
