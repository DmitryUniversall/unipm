package com.universall.core.impl

import com.universall.core.api.UniPm
import com.universall.core.api.model.config.AppConfig
import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.config.validation.CoreAppConfigValidator
import com.universall.core.impl.domain.MavenDependenciesResolver

internal class UniPmImpl(
    private val config: AppConfig,
    private val packageInfo: PackageInfo,
    private val resolver: MavenDependenciesResolver,
) : UniPm {
    private val coreConfigValidator = CoreAppConfigValidator()

    override fun run(output: (String) -> Unit) {
        coreConfigValidator.validate(config = config)
        require(value = !config.testMode) { "Test repository mode will be available at stage 3" }

        val dependencies = resolver.resolve(packageInfo = packageInfo)

        output("Direct dependencies of ${packageInfo.name}:${packageInfo.version}:")

        if (dependencies.isEmpty()) {
            output("No direct dependencies.")
            return
        }

        for (dependency in dependencies) {
            val optional = if (dependency.optional == "true") ", optional" else ""

            output("${dependency.groupId}:${dependency.artifactId}:${dependency.version} [scope=${dependency.scope}$optional]")
        }
    }
}
