package com.github.vgrishecko.kmockserver.request

import java.util.*

open class Request(val path: String, val type: Type) {

    var headers: List<Header> = ArrayList()

    lateinit var body: String

    enum class Type {
        GET, POST, DELETE;
    }
}
