package com.github.vgrishecko.kmockserver.matchers

import com.github.vgrishecko.kmockserver.entity.Request
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasMethod(expectedMethod: Request.Method): Matcher<Request> {
    return object : TypeSafeMatcher<Request>() {

        override fun describeTo(description: Description) {
            description.appendText("The HTTP query should have method: $expectedMethod")
        }

        override fun describeMismatchSafely(request: Request, mismatchDescription: Description) {
            mismatchDescription.appendText("\nMethod ${request.method} was found instead.")
        }

        override fun matchesSafely(request: Request): Boolean =
                request.method == expectedMethod
    }
}