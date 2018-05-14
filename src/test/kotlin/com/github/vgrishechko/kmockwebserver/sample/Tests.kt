package com.github.vgrishechko.kmockwebserver.sample

import com.github.vgrishechko.kmockwebserver.testing.Requests
import com.github.vgrishechko.kmockwebserver.testing.assertResponse
import com.github.vgrishechko.kmockwebserver.testing.request
import com.github.vgrishecko.kmockserver.KmockWebServerRule
import com.github.vgrishecko.kmockserver.dsl.thenRespond
import com.github.vgrishecko.kmockserver.dsl.whenever
import com.github.vgrishecko.kmockserver.entity.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain


class Tests {

    val serverRule: KmockWebServerRule = KmockWebServerRule()

    @JvmField
    @Rule
    val rule = RuleChain.emptyRuleChain()
            .around(serverRule)

    @Test
    fun shouldReturnCorrectResponse() {
        val loginResponse = """"{"login":"test", "id":1}"""

        val mockedResponse = success(
                code = 200,
                body = loginResponse,
                headers = headers("Authorization" to "hash", "Test" to "test")
        )

        serverRule.whenever("/login", Request.Method.GET)
                .thenRespond(mockedResponse)

        Requests
                .makeRequest(request("/login", "GET"))
                .assertResponse(mockedResponse)
    }

    @Test
    fun shouldReturn401StatusCode() {
        val mockedResponse = error(401)

        serverRule.whenever("/login", Request.Method.GET)
                .thenRespond(mockedResponse)

        Requests.makeRequest(request("/login", "GET"))
                .assertResponse(mockedResponse)
    }


    @Test
    fun shouldReturn404Response() {
        Requests.makeRequest(request("/", "GET"))
                .assertResponse(404)
    }

    @Test
    fun stressTestshouldAcceptAllRulesAndReturnInTheEnd404() {
        val getRequest = request("/get?a=123", "GET")
        val postRequest = request("/post", "POST", """{"expected_result:"success"}""")

        val getRule: (Request) -> Response? = {
            if (it.method == Request.Method.GET && it.path == "/get") {
                response {
                    code = 200
                }
            } else {
                null
            }
        }

        val postRule: (Request) -> Response? = {
            if (it.method == Request.Method.POST && it.path == "/post") {
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
                Requests.makeRequest(postRequest)
                        .assertResponse(200)
            } else {

                Requests.makeRequest(getRequest)
                        .assertResponse(200)
            }
        }

        Requests.makeRequest(request("/", "GET"))
                .assertResponse(404)
    }

}