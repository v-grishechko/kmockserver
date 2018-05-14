package com.github.vgrishecko.kmockserver.matchers

import com.github.vgrishecko.kmockserver.entity.Request
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun isSentToPath(expectedPath: String): Matcher<Request> {
    return object : TypeSafeMatcher<Request>() {

        override fun describeTo(description: Description) {
            description.appendText("The HTTP query should be sent to: $expectedPath")
        }

        override fun describeMismatchSafely(
                request: Request,
                mismatchDescription: Description
        ) {
            mismatchDescription.appendText("\nRequest sent to ${request.method} instead.")
        }

        override fun matchesSafely(request: Request): Boolean =
                request.path.startsWith(expectedPath)
    }
}