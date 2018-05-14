package com.github.vgrishechko.kmockwebserver.testing

import com.github.vgrishecko.kmockserver.entity.Header
import com.github.vgrishecko.kmockserver.entity.Headers
import okhttp3.Response
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat

fun Response.assertResponse(code: Int, headers: Headers? = null, body: String? = null) {
    Assertions.assertThat(code).isEqualTo(code()).describedAs("Response request should be equal to $code")

    if(headers != null) {
        for(header in headers) {
            assertThat(header(header.key)).isEqualTo(header.value).describedAs("Response should contain header $header")
        }
    }

    if(body != null) {
        assertThat(body).isEqualToIgnoringCase(body().string())
    } else {
        assertThat(body().string()).describedAs("Response body should be empty").isNullOrEmpty()
    }
}

fun Response.assertResponse(response: com.github.vgrishecko.kmockserver.entity.Response) {
    assertResponse(response.code, response.headers, response.body)
}

fun headers(vararg headers: Header): Headers {
    val result = ArrayList<Header>()

    for (header in headers) {
        result.add(header)
    }

    return result.toMap()
}