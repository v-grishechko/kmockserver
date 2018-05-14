package com.github.vgrishecko.kmockserver.dsl

import com.github.vgrishecko.kmockserver.MockWebServerRule
import com.github.vgrishecko.kmockserver.entity.Headers
import com.github.vgrishecko.kmockserver.entity.QueryParams
import com.github.vgrishecko.kmockserver.entity.Request
import com.github.vgrishecko.kmockserver.entity.Response
import org.hamcrest.Matcher

fun MockWebServerRule.whenever(sentToPath: String,
                               queryParams: QueryParams? = null,
                               body: String? = null,
                               headers: Headers? = null,
                               method: Request.Method? = null): Pair<MockWebServerRule, Matcher<Request>> {
    return Pair(this, matches(Request(sentToPath, method, queryParams, headers, body)))
}

fun Pair<MockWebServerRule, Matcher<Request>>.thenRespond(response: Response): Pair<MockWebServerRule, Matcher<Request>> {
    this.first.addRule {
        if (this.second.matches(it)) {
            response
        } else {
            null
        }
    }

    return this
}

//TODO write class
/*fun params(vararg pairs: Pair<String, String>): QueryParams =
        if (pairs.isNotEmpty()) pairs.toList().toMap() else listOf()*/

fun headers(vararg pairs: Pair<String, String>): Headers =
        if (pairs.isNotEmpty()) pairs.toMap() else emptyMap()