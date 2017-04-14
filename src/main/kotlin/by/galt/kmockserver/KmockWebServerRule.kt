package by.galt.kmockserver

import by.galt.kmockserver.netty.NettyServer
import by.galt.kmockserver.rule.ResponseRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import rx.Observable
import rx.subjects.BehaviorSubject
import java.net.URL


class KmockWebServerRule : MockWebServerRule {

    val server: NettyServer = NettyServer()

    var responseRules: MutableList<ResponseRule> = ArrayList()

    var stopEvents = BehaviorSubject.create<Unit>()

    var startEvents = BehaviorSubject.create<Unit>()

    override fun start() {
        server.start(responseRules)
        startEvents.onNext(Unit)
    }

    override fun started(): Observable<Unit> {
        return startEvents
    }

    override fun stop() {
        server.stop()
        stopEvents.onNext(Unit)
    }

    override fun stopped(): Observable<Unit> {
        return stopEvents
    }

    override fun addRule(responseRule: ResponseRule) {
        responseRules.add(responseRule)
    }

    override fun removeRule(responseRule: ResponseRule) {
        responseRules.add(responseRule)
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                start()

                try {
                    base?.evaluate()
                } finally {
                    responseRules.clear()
                    stop()
                }
            }
        }
    }

}