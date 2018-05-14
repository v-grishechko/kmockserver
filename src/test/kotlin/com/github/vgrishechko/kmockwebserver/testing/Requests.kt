package com.github.vgrishechko.kmockwebserver.testing

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import org.assertj.core.api.Assertions.assertThat

object Requests {
    val client: OkHttpClient = OkHttpClient()

    fun makeRequest(request: okhttp3.Request): Response {
        return client.newCall(request).execute()
    }
}

fun request(path: String, method: String, jsonBody: String? = null): okhttp3.Request {
    val JSON = MediaType.parse("application/json; charset=utf-8")

    return okhttp3.Request.Builder().apply {
        url("http://127.0.0.1:4512" + path)
        method(method, if (jsonBody != null) RequestBody.create(JSON, jsonBody) else null)
    }.build()
}

