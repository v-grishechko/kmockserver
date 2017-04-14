package by.galt.kmockserver.request

import by.galt.kmockserver.response.Response
import by.galt.kmockserver.request.Header
import java.util.*

abstract class Request(val path: String, val type: Type) {

    var headers: List<Header> = ArrayList()

    var body: String = ""

    var response: Response? = null

    fun headers(vararg headers: Header) {
        val result = ArrayList<Header>()
        for (header in headers) {
            result.add(header)
        }

        this.headers = result
    }

    fun execute(exec: () -> Response?): Response? {
        return exec()
    }

    enum class Type {
        GET, POST
    }
}

class Get(path: String) : Request(path, type = Type.GET)

class Post(path: String) : Request(path, type = Type.POST)
