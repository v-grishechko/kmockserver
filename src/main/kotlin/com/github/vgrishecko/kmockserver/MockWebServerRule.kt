package com.github.vgrishecko.kmockserver

import com.github.vgrishecko.kmockserver.rule.ResponseRule
import org.junit.rules.TestRule
import rx.Observable

internal interface MockWebServerRule : TestRule {

    fun start()

    fun started(): Observable<Unit>

    fun stop()

    fun stopped(): Observable<Unit>

    fun addRule(responseRule: ResponseRule)

    fun removeRule(responseRule: ResponseRule)

}