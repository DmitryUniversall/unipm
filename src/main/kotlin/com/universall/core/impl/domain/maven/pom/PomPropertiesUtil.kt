package com.universall.core.impl.domain.maven.pom

import com.universall.core.impl.domain.model.maven.MavenDependency

internal class PomPropertiesUtil {
    fun substitutePropertiesToDependency(
        dependency: MavenDependency,
        properties: Map<String, String>,
    ): MavenDependency = dependency.copy(
        groupId = substituteProperties(value = dependency.groupId, properties = properties),
        artifactId = substituteProperties(value = dependency.artifactId, properties = properties),
        version = dependency.version?.let {
            substituteProperties(value = it, properties = properties)
        },
        type = dependency.type?.let { substituteProperties(value = it, properties = properties) },
        classifier = dependency.classifier?.let {
            substituteProperties(value = it, properties = properties)
        },
        scope = dependency.scope?.let { substituteProperties(value = it, properties = properties) },
        optional = dependency.optional?.let {
            substituteProperties(value = it, properties = properties)
        },
    )

    fun substituteProperties(value: String, properties: Map<String, String>): String =
        substituteProperties(
            value = value,
            properties = properties,
            visiting = emptySet(),
        )

    private fun substituteProperties(
        value: String,
        properties: Map<String, String>,
        visiting: Set<String>,
    ): String = propertyReference.replace(input = value) { match ->
        val name = match.groupValues[1]
        require(value = name !in visiting) { "Cyclic POM property: $name" }

        val replacement = requireNotNull(value = properties[name]) {
            "Unresolved POM property: $name"
        }

        substituteProperties(
            value = replacement,
            properties = properties,
            visiting = visiting + name,
        )
    }

    private companion object {
        val propertyReference = Regex(pattern = "\\$\\{([^}]+)}")
    }
}
