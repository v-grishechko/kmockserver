package com.github.vgrishechko.kmockwebserver.sample

import com.github.vgrishecko.kmockserver.KmockWebServerRule
import com.github.vgrishecko.kmockserver.request.Header
import com.github.vgrishecko.kmockserver.request.Request
import com.github.vgrishecko.kmockserver.response.Response
import com.github.vgrishecko.kmockserver.response.response
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain


class SampleTest {

    val serverRule: KmockWebServerRule = KmockWebServerRule()

    val client: OkHttpClient = OkHttpClient()

    @JvmField @Rule
    val rule = RuleChain.emptyRuleChain()
            .around(serverRule)

    @Test
    fun shouldReturnCorrectResponse() {
        val loginResponse = """"{"login":"test", "id":1}"""

        serverRule.addRule { request ->
            if (request.path == "/login" && request.type == Request.Type.GET) {
                response {
                    code = 200
                    body = loginResponse
                    headers(Header("Authorization", "hash"), Header("Test", "test"))
                }
            } else {
                null
            }
        }

        val request = mockRequest("/login", "GET")

        client.newCall(request).execute().apply {
            assertThat(code()).isEqualTo(200)
            assertThat(body().string()).isEqualToIgnoringCase(loginResponse)
            assertThat(header("Authorization")).isEqualToIgnoringCase("hash")
            assertThat(header("Test")).isEqualToIgnoringCase("test")
        }

        assertThat(client.newCall(request).execute().code()).isEqualTo(404)
    }

    @Test
    fun shouldReturn401StatusCode() {
        serverRule.addRule { request ->
            if (request.path == "/login" && request.type == Request.Type.GET) {
                response {
                    code = 401
                }
            } else {
                null
            }
        }

        client.newCall(mockRequest("/login", "GET")).execute().apply {
            assertThat(code()).isEqualTo(401)
            assertThat(body().string()).isEmpty()
        }
    }

    @Test
    fun shouldReturn404Response() {
        assertThat(client.newCall(mockRequest("/", "GET")).execute().code()).isEqualTo(404)
    }

    @Test
    fun stressTestshouldAcceptAllRulesAndReturnInTheEnd404() {
        val getRequest = mockRequest("/get?a=123", "GET")
        val postRequest = mockRequest("/post", "POST", """{"expected_result:"success"}""")

        val getRule: (Request) -> Response? = {
            if (it.type == Request.Type.GET && it.path == "/get") {
                response {
                    code = 200
                }
            } else {
                null
            }
        }

        val postRule: (Request) -> Response? = {
            if (it.type == Request.Type.POST && it.path == "/post") {
                response {
                    code = 200
                }
            } else {
                null
            }
        }

        for (i in 1..100) {
            if (i % 2 == 0) {
                serverRule.addRule(getRule)
            } else {
                serverRule.addRule(postRule)
            }
        }

        for (i in 1..100) {
            if (i % 2 == 0) {
                client.newCall(postRequest).execute().apply {
                    assertThat(code()).isEqualTo(200)
                    assertThat(body().string()).isEmpty()
                }
            } else {
                client.newCall(getRequest).execute().apply {
                    assertThat(code()).isEqualTo(200)
                    assertThat(body().string()).isEmpty()
                }
            }
        }

        assertThat(client.newCall(getRequest).execute().code()).isEqualTo(404)
    }

    fun mockRequest(path: String, method: String, jsonBody: String? = null): okhttp3.Request {
        val JSON = MediaType.parse("application/json; charset=utf-8")

        return okhttp3.Request.Builder().apply {
            url("http://127.0.0.1:8080" + path)
            method(method, if (jsonBody != null) RequestBody.create(JSON, jsonBody) else null)
        }.build()
    }

}