package com.github.vgrishecko.kmockserver.dsl

import com.github.vgrishecko.kmockserver.MockWebServerRule
import com.github.vgrishecko.kmockserver.entity.Headers
import com.github.vgrishecko.kmockserver.entity.QueryParams
import com.github.vgrishecko.kmockserver.entity.Request
import com.github.vgrishecko.kmockserver.entity.Response
import com.github.vgrishecko.kmockserver.matchers.matches
import org.hamcrest.Matcher

fun MockWebServerRule.whenever(sentToPath: String,
                               method: Request.Method): Pair<MockWebServerRule, Matcher<Request>> {
    return Pair(this, matches(Request(sentToPath, method, null, null, null)))
}

fun MockWebServerRule.whenever(sentToPath: String,
                               method: Request.Method,
                               body: String): Pair<MockWebServerRule, Matcher<Request>> {
    return Pair(this, matches(Request(sentToPath, method, null, null, body)))
}

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