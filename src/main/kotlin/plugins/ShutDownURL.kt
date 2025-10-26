package com.hamzehk.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ShutDownUrl

fun Application.configureShutDownURL(){

    install(ShutDownUrl.ApplicationCallPlugin){
        shutDownUrl = "/shutdown"
        exitCodeSupplier = { 0 }
    }
}