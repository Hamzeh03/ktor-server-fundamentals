package com.hamzehk.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.httpMethod
import io.ktor.server.request.path
import org.slf4j.event.Level

fun Application.configureCallLogging(){

    install(CallLogging){

        level = Level.INFO

        // with filter, we choose which API we want to log
        filter {
            call -> call.request.path().startsWith("/hi")
        }

        // OutPut EX : INFO  Application - UserId UnKnown, Method: GET, Path: /hi
        format {
            call ->
            val userId = call.request.queryParameters["userId"] ?: "UnKnown"
            "UserId $userId, Method: ${call.request.httpMethod.value}, Path: ${call.request.path()}"
        }
    }
}