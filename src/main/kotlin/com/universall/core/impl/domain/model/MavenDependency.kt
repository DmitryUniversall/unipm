package com.universall.core.impl.domain.model

internal data class MavenDependency(
    val groupId: String,
    val artifactId: String,
    val version: String? = null,
    val type: String? = null,
    val classifier: String? = null,
    val scope: String? = null,
    val optional: String? = null,
)
