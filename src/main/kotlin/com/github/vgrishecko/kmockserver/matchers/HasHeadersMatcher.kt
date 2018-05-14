package com.github.vgrishecko.kmockserver.matchers

import com.github.vgrishecko.kmockserver.entity.Header
import com.github.vgrishecko.kmockserver.entity.Request
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasHeaders(headers: Map<String, String>): Matcher<Request> {
    return object : TypeSafeMatcher<Request>() {

        override fun describeTo(description: Description) {
            description.appendText("The HTTP query should contain the following headers: ")
            headers.forEach {
                description.appendText("\n${it.key} = ${it.value}")
            }
        }

        override fun describeMismatchSafely(request: Request, mismatchDescription: Description) {
            for ((name, value) in headers) {
                val header = request.getHeader(name)
                if (header == null) {
                    mismatchDescription.appendText("\nheader $name is not present.")
                } else {
                    if (header != value) {
                        mismatchDescription.appendText("\n$name = $header (Not matching!)")
                    }
                }
            }
        }

        override fun matchesSafely(request: Request): Boolean {
            var failed = false
            for ((key, value) in headers) {
                val header = request.getHeader(key)
                if (header == null) {
                    failed = true
                } else {
                    if (header != value) {
                        failed = true
                    }
                }
            }
            return !failed
        }
    }
}