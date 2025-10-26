package com.hamzehk

import com.hamzehk.plugins.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val httpClient = HttpClient(CIO) {
        // Configure the HttpClient if needed
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val jwt = environment.config.config("ktor.jwt")


    val secret = jwt.property("secret").getString()
    val issuer = jwt.property("issuer").getString()
    val audience = jwt.property("audience").getString()
    val realm = jwt.property("realm").getString()
    val tokenExpiry = jwt.property("expire").getString().toLong()


    val config = JWTConfig(
        secret = secret,
        issuer = issuer,
        audience = audience,
        realm = realm,
        tokenExpiry = tokenExpiry
    )

    configureCallLogging()            // Video 19 - Call Logging
    configureResources()              // Early setup  - Serving static content (resources, files)
    configureRateLimit()              // Video 7 - Rate limiting requests
    //configureBasicAuthentication()  // Video 10 - Basic Authentication
    //configureBearerAuthentication() // Video 12 - Bearer Token Authentication
    configureSessions()               // Video 13 - Sessions & Cookies
    //configureSessionAuthentication()// Video 13 - Session Authentication
    configureJWTAuthentication(config, httpClient)  // Video 14 - JWT Authentication
    //configureGoogleOAuth(httpClient)// Video 15 - OAuth - Authentication We dont need to add this
    configureSSE()                    // Video 16 - Server-Sent Events (SSE)
    configureWebsockets()             // Video 17 - WebSockets Chat
    configureRouting(config, httpClient)  // Shared routing setup (used across all videos)
    configureSerialization()          // Early setup - JSON serialization
    configureStatusPages()            // Video 5 - Status pages (error handling)
    configureRequestValidation()      // Video 6 - Request validation
    configurePartialContent()         // Video 8 - Partial content responses
    configureAutoHeadResponse()       // Video 8 - Auto HEAD responses
    configureShutDownURL()            // Video 18 - Shutdown URL
    configureCustomHeader()           // Video 20 - Custom Plugin

}