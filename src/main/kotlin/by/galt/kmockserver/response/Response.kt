package by.galt.kmockserver.response

import by.galt.kmockserver.request.Header
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