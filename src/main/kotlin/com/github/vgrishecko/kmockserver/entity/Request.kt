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


fun params(vararg pairs: Pair<String, String>): QueryParams {
    val params: MutableMap<String, MutableList<String>> = HashMap()

    for(pair in pairs) {
        if(!params.containsKey(pair.first)) {
            params[pair.first] = ArrayList()
        }

        params[pair.first]?.add(pair.second)
    }

    return params
}

fun headers(vararg pairs: Pair<String, String>): Headers =
        if (pairs.isNotEmpty()) pairs.toMap() else emptyMap()