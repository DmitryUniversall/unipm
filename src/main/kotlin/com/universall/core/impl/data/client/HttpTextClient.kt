package com.universall.core.impl.data.client

import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

internal class HttpTextClient(
    private val client: HttpClient,
) {
    fun <T> get(uri: URI, decode: (String) -> T): T {
        val request = HttpRequest.newBuilder(uri)
            .timeout(Duration.ofSeconds(30))
            .GET()
            .build()

        val response = try {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (exception: IOException) {
            throw IllegalArgumentException("Cannot load '$uri': ${exception.message}", exception)
        } catch (exception: InterruptedException) {
            Thread.currentThread().interrupt()
            throw IllegalArgumentException("Loading '$uri' was interrupted", exception)
        }

        require(value = response.statusCode() in 200..299) { "Cannot load '$uri': HTTP ${response.statusCode()}" }

        return try {
            decode(response.body())
        } catch (exception: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid response from '$uri': ${exception.message}", exception)
        }
    }
}
