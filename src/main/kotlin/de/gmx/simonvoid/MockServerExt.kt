package de.gmx.simonvoid

import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.netty.MockServer


/**
 * @param isSecure whether to use https or http
 * @param host the host to bind to
 * @param port the port to bind to. Used as local and remote port (@see[MockServer.getLocalPorts], @see[MockServer.remoteSocket]])
 * @param givenRequest the request to match, @see [HttpRequest]
 * @param returnResponse the response to return, @see [HttpResponse]
 * @param test the test to run, the mock server url is passed as parameter. The server will be closed after the test.
 *
 */
fun useMockServer(
    isSecure: Boolean = true,
    host: String = "localhost",
    port: Int = 1080,
    givenRequest: HttpRequest.() -> Unit,
    returnResponse: HttpResponse.() -> Unit,
    test: (mockServerUrl: String) -> Unit
) {
    /** Using *see[port] as local and remote port in the context of the mock server.
        It seems to work as intended, so I don't overlook all the implications.*/
    ClientAndServer.startClientAndServer(host, port, port).use { mockServer: ClientAndServer ->
        mockServer.`when`(
            HttpRequest.request().apply{
                givenRequest()
                // setting this option automatically is more convenient, but a bit magic
                withSecure(isSecure)
            }
        ).respond(HttpResponse.response().apply(returnResponse))
        val mockServerUrl = buildString {
            append(if (isSecure) "https" else "http")
            append("://$host:$port")
        }
        test(mockServerUrl)
    }
}