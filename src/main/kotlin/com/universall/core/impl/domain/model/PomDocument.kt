package com.universall.core.impl.domain.model

internal data class PomDocument(
    val groupId: String?,
    val artifactId: String,
    val version: String?,
    val properties: Map<String, String>,
    val dependencies: List<MavenDependency>,
)
