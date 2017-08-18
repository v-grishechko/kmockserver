package com.github.vgrishecko.kmockserver.netty

import com.github.vgrishecko.kmockserver.request.Header
import com.github.vgrishecko.kmockserver.response.Response
import com.github.vgrishecko.kmockserver.request.Request
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.RxNetty
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import rx.Observable
import java.nio.charset.Charset
import java.util.function.Function

class NettyServer {

    var server: HttpServer<ByteBuf, ByteBuf>? = null

    var responseRules: MutableList<(Request) -> Response?> = ArrayList()

    var isPause = false

    fun start() {
        stop()

        server = RxNetty.createHttpServer(8080, { request, serverResponse ->
            request.content
                    .map {
                        (it as ByteBuf).toString(Charset.defaultCharset())
                    }
                    .map {
                        request.convertToRequest(it)
                    }
                    .map { request ->
                        responseRules.firstOrNull {
                            if (it.invoke(request) != null) {
                                responseRules.remove(it)
                                true
                            } else {
                                false
                            }
                        }?.invoke(request)
                    }
                    .flatMap { response ->
                        while (isPause) {
                        }

                        if (response != null) {
                            response.headers.forEach { serverResponse.headers.addHeader(it.name, it.value) }
                            serverResponse.status = HttpResponseStatus(response.code, "")

                            if (response.body.isNotEmpty()) {
                                serverResponse.writeString(response.body)
                            }

                            serverResponse.close()
                        } else {
                            serverResponse.status = HttpResponseStatus.NOT_FOUND
                            serverResponse.close()
                        }
                    }.
                    ignoreElements()
                    .doOnError({ e ->
                        e.printStackTrace()
                    })


        })

        server?.start()
    }

    fun stop() {
        server?.shutdown()
        server = null
    }
}

fun HttpServerRequest<*>.convertToRequest(body: String): Request {
    val request = Request(this.path, Request.Type.valueOf(this.httpMethod.name()))
    request.body = body
    this.headers.names().forEach {
        request.headers.plus(Header(it, this.headers[it]))
    }

    return request
}

