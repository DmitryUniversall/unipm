package com.universall.core.impl.domain.maven.pom

import com.universall.core.impl.domain.maven.validation.PackageInfoValidator
import com.universall.core.impl.domain.model.maven.MavenCoordinates
import com.universall.core.impl.domain.model.maven.MavenDependency
import com.universall.core.impl.domain.model.maven.PomDocument
import com.universall.core.impl.domain.model.pkg.PackageCoordinates

internal class PomDependenciesResolver {
    private val validator = PackageInfoValidator()
    private val propertiesUtil = PomPropertiesUtil()

    fun resolve(document: PomDocument, coordinates: MavenCoordinates): List<PackageCoordinates> {
        val properties = buildPropertiesToSubstitute(document = document, coordinates = coordinates)
        validateDocument(document = document, coordinates = coordinates, properties = properties)

        return document.dependencies.map { dependency ->
            val substituted = propertiesUtil.substitutePropertiesToDependency(
                dependency = dependency,
                properties = properties,
            )

            validateDependency(dependency = substituted)
        }
    }

    private fun buildPropertiesToSubstitute(
        document: PomDocument,
        coordinates: MavenCoordinates,
    ): Map<String, String> = document.properties + mapOf(
        "project.groupId" to coordinates.groupId,
        "project.artifactId" to coordinates.artifactId,
        "project.version" to coordinates.version,
        "pom.groupId" to coordinates.groupId,
        "pom.artifactId" to coordinates.artifactId,
        "pom.version" to coordinates.version,
    )

    private fun validateDocument(
        document: PomDocument,
        coordinates: MavenCoordinates,
        properties: Map<String, String>,
    ) {
        val groupId = document.groupId?.let {
            propertiesUtil.substituteProperties(value = it, properties = properties)
        } ?: coordinates.groupId

        val artifactId = propertiesUtil.substituteProperties(
            value = document.artifactId,
            properties = properties,
        )

        val version = document.version?.let {
            propertiesUtil.substituteProperties(value = it, properties = properties)
        } ?: coordinates.version

        require(
            value = groupId == coordinates.groupId && artifactId == coordinates.artifactId &&
                version == coordinates.version,
        ) {
            "POM coordinates do not match ${coordinates.name}:${coordinates.version}"
        }
    }

    private fun validateDependency(dependency: MavenDependency): PackageCoordinates {
        val name = "${dependency.groupId}:${dependency.artifactId}"
        val version = requireNotNull(value = dependency.version) {
            "Missing version for dependency $name"
        }

        validator.validateCoordinates(
            coordinates = MavenCoordinates(
                groupId = dependency.groupId,
                artifactId = dependency.artifactId,
                version = version,
            ),
        )

        val scope = dependency.scope ?: "compile"
        val optional = dependency.optional ?: "false"
        require(value = scope in scopes) { "Unsupported dependency scope for $name: $scope" }
        require(value = optional == "true" || optional == "false") {
            "Invalid optional value for $name: $optional"
        }

        return PackageCoordinates(name = name, version = version)
    }

    private companion object {
        val scopes = setOf("compile", "runtime", "provided", "test", "system")
    }
}
