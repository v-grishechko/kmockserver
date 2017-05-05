package com.github.vgrishecko.kmockserver.rule

import com.github.vgrishecko.kmockserver.response.Response
import com.github.vgrishecko.kmockserver.request.Header
import java.util.*

abstract class ResponseRule(val path: String, val type: Type) {

    var headers: List<Header> = ArrayList()

    var body: String = ""

    lateinit var response: Response

    fun headers(vararg headers: Header) {
        val result = ArrayList<Header>()
        for (header in headers) {
            result.add(header)
        }

        this.headers = result
    }

    fun response(init: Response.() -> Unit) {
        val response = Response()
        response.init()
        this.response = response
    }

    fun execute(exec: () -> Response?): Response? {
        return exec()
    }

    enum class Type {
        GET, POST
    }
}

class Get(path: String) : ResponseRule(path, type = Type.GET)

class Post(path: String) : ResponseRule(path, type = Type.POST)

fun get(url: String, init: Get.() -> Unit): ResponseRule {
    val request = Get(url)
    request.init()
    return request
}

fun post(url: String, init: Post.() -> Unit): ResponseRule {
    val request = Post(url)
    request.init()
    return request
}
