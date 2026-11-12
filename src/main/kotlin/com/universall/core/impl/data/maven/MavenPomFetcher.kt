package com.universall.core.impl.data.maven

import com.universall.core.impl.data.client.HttpTextClient
import com.universall.core.impl.domain.PomParser
import com.universall.core.impl.domain.model.MavenCoordinates
import com.universall.core.impl.domain.model.PomDocument
import java.net.URI

internal class MavenPomFetcher(
    private val httpClient: HttpTextClient,
    private val parser: PomParser,
) {
    fun fetch(repository: String, coordinates: MavenCoordinates): PomDocument {
        val groupPath = coordinates.groupId.replace(oldChar = '.', newChar = '/')
        val artifactPath = "$groupPath/${coordinates.artifactId}/${coordinates.version}"
        val fileName = "${coordinates.artifactId}-${coordinates.version}.pom"

        val uri = URI.create("${repository.trimEnd { it == '/' }}/$artifactPath/$fileName")

        return httpClient.get(uri = uri) { text -> parser.parse(text = text) }
    }
}
