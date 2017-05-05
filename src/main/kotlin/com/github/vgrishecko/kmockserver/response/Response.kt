package com.github.vgrishecko.kmockserver.response

import com.github.vgrishecko.kmockserver.request.Header
import java.util.*

class Response {

    var headers : MutableList<Header> = ArrayList()

    var body: String = ""

    var code: Int = 200

    fun headers(vararg headers: Header) {
        val result = ArrayList<Header>()

        for (header in headers) {
            result.add(header)
        }

        this.headers.addAll(result)
    }

}