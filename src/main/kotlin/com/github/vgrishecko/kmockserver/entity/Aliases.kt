package com.github.vgrishecko.kmockserver.entity

typealias Headers = Map<String, String>

typealias ServerRule = (Request) -> Response?

typealias QueryParams = Map<String, List<String>>

typealias Body = String

typealias Header = Pair<String, String>