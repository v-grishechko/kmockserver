package com.github.vgrishecko.kmockserver.entity

data class Response(val headers: Headers?,
                    val body: Body?,
                    val code: Int) {

    class Builder {

        var headers: Headers = HashMap()

        var body: String = ""

        var code: Int = 200

        fun headers(vararg headers: Header) {
            val result = ArrayList<Header>()

            for (header in headers) {
                result.add(header)
            }

            this.headers = result.toMap()
        }

    }
}

fun response(init: Response.Builder.() -> Unit): Response {
    val responseBuilder = Response.Builder()
    responseBuilder.init()
    return Response(responseBuilder.headers, responseBuilder.body, responseBuilder.code)
}

fun success(code: Int = 200,
            body: String? = null,
            headers: Headers = emptyMap()): Response {
    return Response(headers, body, code)
}

fun error(code: Int = 500,
          body: String? = null,
          headers: Headers = emptyMap()): Response {
    return Response(headers, body, code)
}
