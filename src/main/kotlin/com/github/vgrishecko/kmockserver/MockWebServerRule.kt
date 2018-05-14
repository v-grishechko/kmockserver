package com.github.vgrishecko.kmockserver

import com.github.vgrishecko.kmockserver.entity.Request
import com.github.vgrishecko.kmockserver.entity.Response
import org.junit.rules.TestRule
import rx.Observable

interface MockWebServerRule : TestRule {

    fun start()

    fun started(): Observable<Unit>

    fun stop()

    fun stopped(): Observable<Unit>

    fun addRule(responseRule: Rule)

    fun removeRule(responseRule: Rule)

    fun pause()

    fun resume()
}

typealias Rule = (Request) -> Response?