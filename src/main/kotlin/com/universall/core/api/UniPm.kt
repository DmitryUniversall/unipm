package com.universall.core.api

import com.universall.core.api.model.config.AppConfig
import com.universall.core.api.model.pkg.PackageInfo
import com.universall.core.impl.UniPmImpl
import com.universall.core.impl.data.client.HttpTextClient
import com.universall.core.impl.data.maven.MavenPomFetcher
import com.universall.core.impl.domain.MavenDependenciesResolver
import com.universall.core.impl.domain.PomParser
import java.net.http.HttpClient

interface UniPm {
    fun run(output: (String) -> Unit)

    companion object {
        fun create(config: AppConfig, packageInfo: PackageInfo, httpClient: HttpClient): UniPm {
            val pomLoader = MavenPomFetcher(httpClient = HttpTextClient(client = httpClient), parser = PomParser())
            val resolver = MavenDependenciesResolver(pomFetcher = pomLoader)

            return UniPmImpl(config = config, packageInfo = packageInfo, resolver = resolver)
        }
    }
}
