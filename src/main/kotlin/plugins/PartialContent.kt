package com.hamzehk.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.partialcontent.PartialContent

fun Application.configurePartialContent() {
    install(PartialContent)
}