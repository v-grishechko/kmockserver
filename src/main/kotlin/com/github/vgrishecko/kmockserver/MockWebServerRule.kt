package com.github.vgrishecko.kmockserver

import com.github.vgrishecko.kmockserver.request.Request
import com.github.vgrishecko.kmockserver.response.Response
import org.junit.rules.TestRule
import rx.Observable

internal interface MockWebServerRule : TestRule {

    fun start()

    fun started(): Observable<Unit>

    fun stop()

    fun stopped(): Observable<Unit>

    fun addRule(responseRule: (Request) -> Response?)

    fun removeRule(responseRule: (Request) -> Response?)

    fun pause()

    fun resume()

}