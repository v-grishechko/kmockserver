package by.galt.kmockwebserver.sample

import by.galt.kmockserver.KmockWebServerRule
import by.galt.kmockserver.rule.ResponseRule
import by.galt.kmockserver.rule.rule
import by.galt.kmockserver.request.Header
import okhttp3.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import kotlin.test.assertTrue


class SampleTest {

    val serverRule: KmockWebServerRule = KmockWebServerRule()

    val client: OkHttpClient = OkHttpClient()

    @JvmField @Rule
    val rule = RuleChain.emptyRuleChain()
            .around(serverRule)

    @Test
    fun shouldReturnCorrectResponse() {
        val loginResponse = """"{"login":"test", "id":1}"""

        serverRule.addRule(rule {
            get("/login") {
                response {
                    code = 200
                    body = loginResponse
                    headers(Header("Authorization", "hash"), Header("Test", "test"))
                }
            }
        })

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
        serverRule.addRule(rule {
            get("/login") {
                response {
                    code = 401
                }
            }
        })

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

        val getRule = rule {
            get("/get") {
                response {
                    code = 200
                }
            }
        }

        val postRule = rule {
            post("/post") {
                response {
                    code = 200
                }
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

    fun mockRequest(path: String, method: String, jsonBody: String? = null): Request {
        val JSON = MediaType.parse("application/json; charset=utf-8")

        return Request.Builder().apply {
            url("http://127.0.0.1:8080" + path)
            method(method, if (jsonBody != null) RequestBody.create(JSON, jsonBody) else null)
        }.build()
    }

}