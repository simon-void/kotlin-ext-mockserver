package de.gmx.simonvoid.util

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Simple HttpClient that only relies on Java standard lib (no other dependency) but is unsecure
 * because of its ignorance of certificates. Only Ok to use in test!
 */
object UnsecureHttpClient {
    private val httpClient: HttpClient = HttpClient.newHttpClient()
    private val httpsClient: HttpClient = HttpClient.newBuilder().apply {
        sslContext(SSLContext.getInstance("TLS").apply { init(null, trustAllCerts, SecureRandom()) })
    }.build()

    fun sendGetRequestTo(url: String): HttpResponse<String?> {
        val httpClient = when {
            url.startsWith("http://") -> httpClient
            url.startsWith("https://") -> httpsClient
            else -> error("url expected to start with http:// or https:// but didn't! url: $url")
        }

        val req = HttpRequest.newBuilder()
            .uri(URI(url))
            .GET()
            .build()

        return httpClient.send(req, HttpResponse.BodyHandlers.ofString())
    }
}

private val trustAllCerts = arrayOf<TrustManager>(
    object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate>? = null
        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    }
)