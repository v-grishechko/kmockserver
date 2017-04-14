package by.galt.kmockserver.rule

import by.galt.kmockserver.response.Response
import by.galt.kmockserver.request.Get
import by.galt.kmockserver.request.Post
import by.galt.kmockserver.request.Request

class ResponseRule {

    lateinit var request: Request
    lateinit var response: Response

    fun get(url: String, init: Get.() -> Unit) {
        val request = Get(url)
        request.init()
        this.request = request
    }

    fun post(url: String, init: Post.() -> Unit) {
        val request = Post(url)
        request.init()
        this.request = request
    }

    fun response(init: Response.() -> Unit) {
        val response = Response()
        response.init()
        this.response = response
    }
}

fun rule(func: ResponseRule.() -> Unit): ResponseRule {
    val rule = ResponseRule()
    rule.func()
    return rule
}

