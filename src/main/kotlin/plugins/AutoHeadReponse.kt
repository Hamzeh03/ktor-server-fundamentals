package com.hamzehk.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.AutoHeadResponse

fun Application.configureAutoHeadResponse() {
    install(AutoHeadResponse)
}