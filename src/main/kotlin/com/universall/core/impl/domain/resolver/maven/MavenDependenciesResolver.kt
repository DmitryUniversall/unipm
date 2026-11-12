package com.universall.core.impl.domain.resolver.maven

import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.data.maven.MavenPomFetcher
import com.universall.core.impl.domain.maven.pom.PomDependenciesResolver
import com.universall.core.impl.domain.maven.validation.PackageInfoValidator
import com.universall.core.impl.domain.model.maven.MavenCoordinates
import com.universall.core.impl.domain.model.pkg.PackageCoordinates
import com.universall.core.impl.domain.resolver.DependenciesResolver

internal class MavenDependenciesResolver(
    private val packageInfo: PackageInfo,
    private val pomFetcher: MavenPomFetcher,
) : DependenciesResolver {
    private val validator = PackageInfoValidator()
    private val pomResolver = PomDependenciesResolver()

    override fun getRoot(): PackageCoordinates {
        val coordinates = createCoordinates(name = packageInfo.name, version = packageInfo.version)
        validator.validate(packageInfo = packageInfo, coordinates = coordinates)

        return PackageCoordinates(name = coordinates.name, version = coordinates.version)
    }

    override fun resolve(coordinates: PackageCoordinates): List<PackageCoordinates> {
        val mavenCoordinates = createCoordinates(name = coordinates.name, version = coordinates.version)
        validator.validateCoordinates(coordinates = mavenCoordinates)

        val document = pomFetcher.fetch(repository = packageInfo.repository, coordinates = mavenCoordinates)
        return pomResolver.resolve(document = document, coordinates = mavenCoordinates)
    }

    private fun createCoordinates(name: String, version: String): MavenCoordinates {
        require(value = packageName.matches(input = name)) { "Package name must have the form groupId:artifactId" }

        return MavenCoordinates(
            groupId = name.substringBefore(delimiter = ":"),
            artifactId = name.substringAfter(delimiter = ":"),
            version = version,
        )
    }

    private companion object {
        val packageName = Regex(pattern = "[A-Za-z0-9_][A-Za-z0-9_.-]*:[A-Za-z0-9_][A-Za-z0-9_.-]*")
    }
}
