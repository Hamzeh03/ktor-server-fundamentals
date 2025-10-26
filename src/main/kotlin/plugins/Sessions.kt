package com.hamzehk.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import kotlinx.serialization.Serializable

fun Application.configureSessions() {

    install(Sessions){
        cookie<UserSession>("user_session"){
            cookie.path = "/"
            cookie.maxAgeInSeconds = 300 // Session expires in 5 minutes
        }
    }

}

@Serializable
data class UserSession(val username: String)