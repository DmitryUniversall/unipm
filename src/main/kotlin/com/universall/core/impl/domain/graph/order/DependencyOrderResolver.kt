package com.universall.core.impl.domain.graph.order

import com.universall.core.impl.domain.model.graph.DependencyGraph
import com.universall.core.impl.domain.model.pkg.PackageCoordinates

internal class DependencyOrderResolver {
    fun resolve(graph: DependencyGraph): List<PackageCoordinates> {
        val order = mutableListOf<PackageCoordinates>()
        val active = mutableSetOf<PackageCoordinates>()
        val completed = mutableSetOf<PackageCoordinates>()
        val stack = ArrayDeque<TraversalStep>()
        stack.addLast(element = TraversalStep(coordinates = graph.root))

        while (stack.isNotEmpty()) {
            val step = stack.removeLast()
            if (step.coordinates in completed) continue

            if (step.complete) {
                active.remove(element = step.coordinates)
                completed.add(element = step.coordinates)
                order.add(element = step.coordinates)
                continue
            }

            require(value = active.add(element = step.coordinates)) { "Cannot determine dependency loading order: cycle at '${step.coordinates}'" }
            stack.addLast(element = TraversalStep(coordinates = step.coordinates, complete = true))

            for (dependency in graph.dependencies.getValue(key = step.coordinates).asReversed()) {
                stack.addLast(element = TraversalStep(coordinates = dependency))
            }
        }

        return order
    }
}

private data class TraversalStep(
    val coordinates: PackageCoordinates,
    val complete: Boolean = false,
)
