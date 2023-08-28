package de.gmx.simonvoid

import de.gmx.simonvoid.util.UnsecureHttpClient
import org.mockserver.model.MediaType
import kotlin.test.DefaultAsserter.assertEquals
import kotlin.test.Test

class UseMockServerUtilTest {
    private val httpClient = UnsecureHttpClient

    @Test
    fun `verify that useMockServer works as intended`() {

        useMockServer(
            givenRequest = {
                withMethod("GET")
                withPath("/somePath")
            },
            returnResponse = {
                withStatusCode(200)
                withBody("someBody", MediaType.TEXT_PLAIN)
            }
        ) { mockServerUrl ->

            val mockServerResponse = httpClient.sendGetRequestTo("$mockServerUrl/somePath")
            assertEquals("body", "someBody", mockServerResponse.body())
            assertEquals("statusCode", 200, mockServerResponse.statusCode())
        }
    }

}
