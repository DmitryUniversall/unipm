package com.universall.core.impl.domain.model

internal data class MavenCoordinates(
    val groupId: String,
    val artifactId: String,
    val version: String,
) {
    val name: String
        get() = "$groupId:$artifactId"
}
