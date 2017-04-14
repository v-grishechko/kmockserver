package by.galt.kmockserver.netty

import by.galt.kmockserver.rule.ResponseRule
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.RxNetty
import io.reactivex.netty.protocol.http.server.HttpServer

class NettyServer {

    var server: HttpServer<ByteBuf, ByteBuf>? = null

    fun start(responseRules: MutableList<ResponseRule>) {
        stop()

        server = RxNetty.createHttpServer(8080, { request, response ->

            val rule = responseRules.firstOrNull {
                request?.path.equals(it.request.path, true) &&
                        request.httpMethod.name().equals(it.request.type.name, true)
            }

            if (rule != null) {
                rule.response.headers.forEach { response.headers.addHeader(it.name, it.value) }
                response.status = HttpResponseStatus(rule.response.code, "")
                response.writeString(rule.response.body)
                responseRules.remove(rule)
                response.close()
            } else {
                response.status = HttpResponseStatus.NOT_FOUND
                response.close()
            }
        })


        server?.start()
    }

    fun stop() {
        server?.shutdown()
    }
}

