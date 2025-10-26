package com.hamzehk.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.respondText
import java.util.Date

fun Application.configureJWTAuthentication(config: JWTConfig , httpClient: HttpClient) {

    install(Authentication) {

        // Configure JWT authentication Video 14
        jwt("jwt-auth") {
            realm = config.realm

            val jwtVerifier = JWT
                .require(Algorithm.HMAC256(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build()

            verifier(jwtVerifier)

            validate { jwtCredential ->
                val username = jwtCredential.payload.getClaim("username").asString()
                if (!username.isNullOrBlank()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respondText("Token is not valid or has expired",
                    status = HttpStatusCode.Unauthorized
                    )
            }

        }

        // You can add other authentication methods here (Google OAuth) Video 15
        configureGoogleOAuth(httpClient)
    }
}

// Function to generate JWT token Video 14
fun generateToken(config: JWTConfig ,username: String): String {

    val token = JWT.create()
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .withClaim("username", username)
        .withExpiresAt(Date(System.currentTimeMillis() + config.tokenExpiry))
        .sign(Algorithm.HMAC256(config.secret))

    return token
}

// JWT configuration data class Video 14
data class JWTConfig(
    val realm: String,
    val secret: String,
    val issuer: String,
    val audience: String,
    val tokenExpiry: Long
)