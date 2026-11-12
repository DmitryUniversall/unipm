package com.universall.core.impl.domain.resolver

import com.universall.core.impl.domain.model.pkg.PackageCoordinates

internal interface DependenciesResolver {
    fun getRoot(): PackageCoordinates

    fun resolve(coordinates: PackageCoordinates): List<PackageCoordinates>
}
