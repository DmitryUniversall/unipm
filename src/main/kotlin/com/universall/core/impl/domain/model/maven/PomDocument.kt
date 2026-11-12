package com.universall.core.impl.domain.model.maven

internal data class PomDocument(
    val groupId: String?,
    val artifactId: String,
    val version: String?,
    val properties: Map<String, String>,
    val dependencies: List<MavenDependency>,
)
