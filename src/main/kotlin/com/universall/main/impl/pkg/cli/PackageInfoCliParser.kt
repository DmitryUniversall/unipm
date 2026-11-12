package com.universall.main.impl.pkg.cli

import com.universall.core.api.model.pkg.PackageInfo

internal class PackageInfoCliParser {
    fun parse(value: String): PackageInfo {
        val separator = value.indexOf(char = '@')
        require(value = separator > 0 && separator < value.lastIndex) { "Expected --package in the form name:version@repository" }

        val coordinates = value.substring(startIndex = 0, endIndex = separator)
        val versionSeparator = coordinates.lastIndexOf(char = ':')
        require(value = versionSeparator > 0 && versionSeparator < coordinates.lastIndex) { "Expected package coordinates in the form name:version" }

        val name = coordinates.substring(startIndex = 0, endIndex = versionSeparator)
        val version = coordinates.substring(startIndex = versionSeparator + 1)
        require(value = name.isNotBlank() && version.isNotBlank()) { "Package name and version must not be blank" }

        return PackageInfo(
            name = name,
            version = version,
            repository = value.substring(startIndex = separator + 1),
        )
    }
}
