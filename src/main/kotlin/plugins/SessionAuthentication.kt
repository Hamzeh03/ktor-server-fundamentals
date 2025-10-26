package com.hamzehk.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respondText

fun Application.configureSessionAuthentication() {
    install(Authentication){
        session<UserSession>("session-auth"){
            validate { session ->
                session
            }
            challenge {
                call.respondText ("Unauthorized: Please log in to access this resource.",
                status = HttpStatusCode.Unauthorized
                )
            }
        }
    }
}