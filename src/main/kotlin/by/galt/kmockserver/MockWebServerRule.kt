package by.galt.kmockserver

import by.galt.kmockserver.rule.ResponseRule
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