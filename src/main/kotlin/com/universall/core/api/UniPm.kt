package com.universall.core.api

import com.universall.core.api.model.config.AppConfig
import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.UniPmImpl
import com.universall.core.impl.data.client.HttpTextClient
import com.universall.core.impl.data.maven.MavenPomFetcher
import com.universall.core.impl.data.test.TestRepositoryLoader
import com.universall.core.impl.domain.graph.DependencyGraphBuilder
import com.universall.core.impl.domain.maven.pom.PomParser
import com.universall.core.impl.domain.resolver.maven.MavenDependenciesResolver
import com.universall.core.impl.domain.resolver.test.TestDependenciesResolver
import kotlinx.serialization.json.Json
import java.net.http.HttpClient

interface UniPm {
    fun run(output: (String) -> Unit)

    companion object {
        fun create(config: AppConfig, packageInfo: PackageInfo, httpClient: HttpClient, json: Json): UniPm {
            val resolver = if (config.testMode) {
                TestDependenciesResolver(packageInfo = packageInfo, loader = TestRepositoryLoader(json = json))
            } else {
                val pomFetcher = MavenPomFetcher(httpClient = HttpTextClient(client = httpClient), parser = PomParser())
                MavenDependenciesResolver(packageInfo = packageInfo, pomFetcher = pomFetcher)
            }

            return UniPmImpl(config = config, graphBuilder = DependencyGraphBuilder(resolver = resolver))
        }
    }
}
