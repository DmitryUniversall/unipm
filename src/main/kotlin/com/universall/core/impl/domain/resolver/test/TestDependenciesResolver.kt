package com.universall.core.impl.domain.resolver.test

import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.data.test.TestRepositoryLoader
import com.universall.core.impl.domain.model.pkg.PackageCoordinates
import com.universall.core.impl.domain.resolver.DependenciesResolver
import java.nio.file.Path

internal class TestDependenciesResolver(
    private val packageInfo: PackageInfo,
    private val loader: TestRepositoryLoader,
) : DependenciesResolver {
    private val packages by lazy { loadPackages() }

    override fun getRoot(): PackageCoordinates {
        val root = parseCoordinates(value = "${packageInfo.name}:${packageInfo.version}")
        require(value = root in packages) { "Package '$root' is missing from the test repository" }

        return root
    }

    override fun resolve(coordinates: PackageCoordinates): List<PackageCoordinates> =
        requireNotNull(value = packages[coordinates]) {
            "Package '$coordinates' is missing from the test repository"
        }

    private fun loadPackages(): Map<PackageCoordinates, List<PackageCoordinates>> {
        require(value = packageInfo.repository.isNotBlank()) {
            "Test repository path must not be blank"
        }

        val packages = loader.load(path = Path.of(packageInfo.repository))
            .mapKeys { (name, _) -> parseCoordinates(value = name) }
            .mapValues { (_, dependencies) -> dependencies.map { parseCoordinates(value = it) } }

        for ((name, dependencies) in packages) {
            for (dependency in dependencies) {
                require(value = dependency in packages) {
                    "Unknown dependency '$dependency' of '$name' in the test repository"
                }
            }
        }

        return packages
    }

    private fun parseCoordinates(value: String): PackageCoordinates {
        require(value = packageCoordinates.matches(input = value)) {
            "Test package must have the form NAME:version, " +
                "with NAME in uppercase Latin letters: $value"
        }

        return PackageCoordinates(
            name = value.substringBefore(delimiter = ":"),
            version = value.substringAfter(delimiter = ":"),
        )
    }

    private companion object {
        val packageCoordinates = Regex(pattern = "[A-Z]+:[A-Za-z0-9][A-Za-z0-9_.+-]*")
    }
}
