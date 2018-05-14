package com.github.vgrishecko.kmockserver.netty

import com.github.vgrishecko.kmockserver.Rule
import com.github.vgrishecko.kmockserver.entity.Header
import com.github.vgrishecko.kmockserver.entity.Request
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.RxNetty
import io.reactivex.netty.protocol.http.server.HttpServer
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import java.nio.charset.Charset

class NettyServer {

    var server: HttpServer<ByteBuf, ByteBuf>? = null

    var responseRules: MutableList<Rule> = ArrayList()

    var isPause = false

    fun start() {
        stop()

        server = RxNetty.createHttpServer(4512, { request, serverResponse ->
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
                            response.headers?.forEach { serverResponse.headers.addHeader(it.key, it.value) }
                            serverResponse.status = HttpResponseStatus(response.code, "")

                            if (response.body != null && response.body.isNotEmpty()) {
                                serverResponse.writeString(response.body)
                            }

                            serverResponse.close()
                        } else {
                            serverResponse.status = HttpResponseStatus.NOT_FOUND
                            serverResponse.close()
                        }
                    }
                    .ignoreElements()
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
    val path = this.path
    val method = Request.Method.valueOf(this.httpMethod.name())
    val headers = this.headers.names().map { it to this.headers.get(it) }.toMap()


    return Request(path, method, this.queryParameters, headers, body)
}

