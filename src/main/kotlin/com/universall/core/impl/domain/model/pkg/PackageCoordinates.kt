package com.universall.core.impl.domain.model.pkg

internal data class PackageCoordinates(
    val name: String,
    val version: String,
) {
    override fun toString(): String = "$name:$version"
}
