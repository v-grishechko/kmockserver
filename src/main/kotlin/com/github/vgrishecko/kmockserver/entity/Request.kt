package com.github.vgrishecko.kmockserver.entity

open class Request(val path: String,
                   val method: Method?,
                   val queryParams: QueryParams?,
                   val headers: Headers?,
                   val body: Body?) {

    enum class Method {
        GET, POST, DELETE;
    }

    fun getHeader(key: String): String? = headers?.get(key)

    fun getParam(key: String): List<String>? = queryParams?.get(key)
}