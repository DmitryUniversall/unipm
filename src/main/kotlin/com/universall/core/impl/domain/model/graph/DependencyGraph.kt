package com.universall.core.impl.domain.model.graph

import com.universall.core.impl.domain.model.pkg.PackageCoordinates

internal data class DependencyGraph(
    val root: PackageCoordinates,
    val dependencies: Map<PackageCoordinates, List<PackageCoordinates>>,
)
