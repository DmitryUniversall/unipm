package com.universall.core.impl.domain

import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.domain.model.MavenCoordinates
import java.net.URI

internal class PackageInfoValidator {
    fun validate(packageInfo: PackageInfo, coordinates: MavenCoordinates) {
        validateCoordinates(coordinates = coordinates)
        validateRepository(value = packageInfo.repository)
    }

    fun validateCoordinates(coordinates: MavenCoordinates) {
        require(value = identifier.matches(input = coordinates.groupId)) { "Invalid Maven groupId: ${coordinates.groupId}" }
        require(value = identifier.matches(input = coordinates.artifactId)) { "Invalid Maven artifactId: ${coordinates.artifactId}" }
        require(value = coordinates.groupId.split(delimiters = charArrayOf('.')).none { it.isEmpty() }) { "Invalid Maven groupId: ${coordinates.groupId}" }

        validateVersion(value = coordinates.version)
    }

    private fun validateVersion(value: String) {
        require(value = packageVersion.matches(input = value)) { "Package version must be a concrete release version: $value" }
        require(value = !value.endsWith(suffix = "SNAPSHOT", ignoreCase = true)) { "SNAPSHOT versions are not supported: $value" }
        require(value = value.uppercase() !in dynamicVersions) { "Package version must be concrete: $value" }
    }

    private fun validateRepository(value: String) {
        val uri = try {
            URI.create(value)
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid repository URL: $value", exception)
        }

        require(value = uri.scheme?.lowercase() in webSchemes && !uri.host.isNullOrBlank()) { "Repository must be an HTTP or HTTPS URL: $value" }
        require(value = uri.userInfo == null && uri.query == null && uri.fragment == null) { "Repository URL must not contain credentials, query or fragment: $value" }
    }

    private companion object {
        val identifier = Regex(pattern = "[A-Za-z0-9_][A-Za-z0-9_.-]*")
        val packageVersion = Regex(pattern = "[A-Za-z0-9][A-Za-z0-9_.+-]*")

        val dynamicVersions = setOf("LATEST", "RELEASE")

        val webSchemes = setOf("http", "https")
    }
}
