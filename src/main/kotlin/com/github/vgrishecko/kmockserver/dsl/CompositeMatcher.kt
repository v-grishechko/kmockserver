package com.github.vgrishecko.kmockserver.dsl

import com.github.vgrishecko.kmockserver.entity.Body
import com.github.vgrishecko.kmockserver.entity.Headers
import com.github.vgrishecko.kmockserver.entity.QueryParams
import com.github.vgrishecko.kmockserver.entity.Request
import com.github.vgrishecko.kmockserver.matchers.hasHeaders
import com.github.vgrishecko.kmockserver.matchers.hasMethod
import com.github.vgrishecko.kmockserver.matchers.hasQueryParams
import com.github.vgrishecko.kmockserver.matchers.isSentToPath
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf

fun <T : Any> T.matches(
        sentToPath: String,
        queryParams: QueryParams? = null,
        body: Body? = null,
        headers: Headers? = null,
        method: Request.Method? = null
): Matcher<Request> {
    val matchers = mutableListOf(isSentToPath("/$sentToPath"))

    queryParams?.let {
        matchers.add(hasQueryParams(queryParams))
    }

    headers?.let {
        matchers.add(hasHeaders(it))
    }
    method?.let {
        matchers.add(hasMethod(method))
    }

    return allOf(matchers)
}

fun <T: Any> T.matches(request: Request): Matcher<Request> {
    return matches(request.path, request.queryParams, request.body, request.headers, request.method)
}