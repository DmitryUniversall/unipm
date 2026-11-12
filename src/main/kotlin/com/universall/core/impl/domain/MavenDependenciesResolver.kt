package com.universall.core.impl.domain

import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.data.maven.MavenPomFetcher
import com.universall.core.impl.domain.model.MavenCoordinates
import com.universall.core.impl.domain.model.MavenDependency
import com.universall.core.impl.domain.model.PomDocument

internal class MavenDependenciesResolver(
    private val pomFetcher: MavenPomFetcher,
) {
    private val validator = PackageInfoValidator()
    private val pomPropertiesUtil = PomPropertiesUtil()

    fun resolve(packageInfo: PackageInfo): List<MavenDependency> {
        require(value = packageName.matches(input = packageInfo.name)) { "Package name must have the form groupId:artifactId" }

        val coordinates = MavenCoordinates(
            groupId = packageInfo.name.substringBefore(delimiter = ":"),
            artifactId = packageInfo.name.substringAfter(delimiter = ":"),
            version = packageInfo.version,
        )

        validator.validate(packageInfo = packageInfo, coordinates = coordinates)

        val document = pomFetcher.fetch(repository = packageInfo.repository, coordinates = coordinates)
        val properties = buildPropertiesToSubstitute(document = document, coordinates = coordinates)

        validateDocument(document = document, coordinates = coordinates, properties = properties)

        return document.dependencies.map { dependency ->
            val substituted = pomPropertiesUtil.substitutePropertiesToDependency(dependency = dependency, properties = properties)

            val resolved = applyDependencyDefaults(dependency = substituted)

            validateResolvedDependency(dependency = resolved)

            resolved
        }
    }

    private fun buildPropertiesToSubstitute(document: PomDocument, coordinates: MavenCoordinates): Map<String, String> = document.properties + mapOf(
        "project.groupId" to coordinates.groupId,
        "project.artifactId" to coordinates.artifactId,
        "project.version" to coordinates.version,
        "pom.groupId" to coordinates.groupId,
        "pom.artifactId" to coordinates.artifactId,
        "pom.version" to coordinates.version,
    )

    private fun validateDocument(document: PomDocument, coordinates: MavenCoordinates, properties: Map<String, String>) {
        val groupId = document.groupId?.let { pomPropertiesUtil.substituteProperties(value = it, properties = properties) } ?: coordinates.groupId
        val artifactId = pomPropertiesUtil.substituteProperties(value = document.artifactId, properties = properties)
        val version = document.version?.let { pomPropertiesUtil.substituteProperties(value = it, properties = properties) } ?: coordinates.version

        require(value = groupId == coordinates.groupId && artifactId == coordinates.artifactId && version == coordinates.version) {
            "POM coordinates do not match ${coordinates.name}:${coordinates.version}"
        }
    }

    private fun applyDependencyDefaults(dependency: MavenDependency): MavenDependency =
        dependency.copy(
            type = dependency.type ?: "jar",
            classifier = dependency.classifier ?: "",
            scope = dependency.scope ?: "compile",
            optional = dependency.optional ?: "false",
        )

    private fun validateResolvedDependency(dependency: MavenDependency) {
        val name = "${dependency.groupId}:${dependency.artifactId}"
        val version = requireNotNull(value = dependency.version) { "Missing version for dependency $name" }

        validator.validateCoordinates(
            coordinates = MavenCoordinates(
                groupId = dependency.groupId,
                artifactId = dependency.artifactId,
                version = version,
            ),
        )

        require(value = dependency.scope in scopes) { "Unsupported dependency scope for $name: ${dependency.scope}" }
        require(value = dependency.optional == "true" || dependency.optional == "false") { "Invalid optional value for $name: ${dependency.optional}" }
    }

    private companion object {
        val packageName = Regex(pattern = "[A-Za-z0-9_][A-Za-z0-9_.-]*:[A-Za-z0-9_][A-Za-z0-9_.-]*")
        val scopes = setOf("compile", "runtime", "provided", "test", "system")
    }
}
