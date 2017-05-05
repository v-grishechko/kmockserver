package com.github.vgrishecko.kmockserver.response

import com.github.vgrishecko.kmockserver.request.Header
import java.util.*

class Response {

    var headers : MutableList<Header> = ArrayList()

    var body: String = ""

    var code: Int = 200

    fun headers(vararg headers: Header) {
        val result = headers.toList()

        this.headers.addAll(result)
    }

}

fun response(init: Response.() -> Unit): Response {
    val response = Response()
    response.init()
    return response
}