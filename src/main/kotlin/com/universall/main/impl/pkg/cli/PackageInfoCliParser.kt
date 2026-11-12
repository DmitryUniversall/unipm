package com.universall.main.impl.pkg.cli

import com.universall.core.api.model.pkg.PackageInfo

internal class PackageInfoCliParser {
    fun parse(value: String): PackageInfo {
        val separator = value.indexOf(char = '@')
        require(value = separator > 0 && separator < value.lastIndex) { "Expected --package in the form group:artifact:version@repository" }

        val coordinates = value.substring(startIndex = 0, endIndex = separator).split(delimiters = charArrayOf(':'))
        require(value = coordinates.size == 3 && coordinates.all { it.isNotBlank() }) { "Expected package coordinates in the form group:artifact:version" }

        return PackageInfo(
            name = "${coordinates[0]}:${coordinates[1]}",
            version = coordinates[2],
            repository = value.substring(startIndex = separator + 1),
        )
    }
}
