package com.universall.main.impl.pkg.json

import com.universall.core.api.model.pkg.PackageInfo
import kotlinx.serialization.Serializable

@Serializable
internal data class JsonPackageInfoDocument(
    val name: String,
    val version: String,
    val repository: String,
) {
    fun toPackageInfo(): PackageInfo = PackageInfo(
        name = name,
        version = version,
        repository = repository,
    )
}
