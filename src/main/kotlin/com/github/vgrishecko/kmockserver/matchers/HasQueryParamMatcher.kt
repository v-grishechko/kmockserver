package com.github.vgrishecko.kmockserver.matchers

import com.github.vgrishecko.kmockserver.entity.QueryParams
import com.github.vgrishecko.kmockserver.entity.Request
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasQueryParams(expectedParams: QueryParams): Matcher<Request> {
    return object : TypeSafeMatcher<Request>() {

        override fun describeTo(description: Description) {
            description.appendText("The HTTP query should contain params: ")
            expectedParams.forEach {
                description.appendText("\n${it.key} = ${it.value}")
            }
        }

        override fun describeMismatchSafely(request: Request, mismatchDescription: Description) {
            for ((key, value) in expectedParams) {
                val requestedParamsForExpectedKey = request.getParam(key)
                if (requestedParamsForExpectedKey == null ||
                        requestedParamsForExpectedKey.isEmpty()) {
                    mismatchDescription.appendText("\nparameter $key is not present.")
                } else {
                    if (requestedParamsForExpectedKey.find { it == key } == null) {
                        mismatchDescription.appendText("\n$key = $value (Not matching!)")
                    }
                }
            }
        }

        override fun matchesSafely(request: Request): Boolean {
            var failed = false
            for ((key, value) in expectedParams) {
                val requestedParamsForExpectedKey = request.getParam(key)
                if (requestedParamsForExpectedKey == null ||
                        requestedParamsForExpectedKey.isEmpty()) {
                    failed = true
                } else {
                    if (requestedParamsForExpectedKey.find { it == key } == null) {
                        failed = true
                    }
                }
            }
            return !failed
        }
    }
}