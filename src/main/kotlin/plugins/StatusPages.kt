package com.hamzehk.plugins

import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {

    // Status Pages Configuration (Video 5)
    install(StatusPages) {

        // Handle InternalServerError(500) exceptions
        exception<Throwable> { call, cause ->
            call.respondText(text = "Internal server error(500) : ${cause.message}", status = InternalServerError)
        }

        // Handle BadRequest(400) status code
        status(HttpStatusCode.BadRequest) { call, cause ->
            call.respondText(
                text = "Bad Request(400) : ${cause.description}",
                status = HttpStatusCode.BadRequest
            )
        }

//        // Handle Unauthorized(401) status code
//        status(HttpStatusCode.Unauthorized) { call, cause ->
//            call.respondText(
//                text = "Unauthorized access(401) : ${cause.description}",
//                status = HttpStatusCode.Unauthorized
//            )
//        }

        // Serve static error400.html file for BadRequest(400) and NotFound(404) and Forbidden(403) status codes
        // The # statusFile function maps specific status codes to a static file
        statusFile(
            HttpStatusCode.BadRequest,
            HttpStatusCode.NotFound,
            HttpStatusCode.Forbidden,
            filePattern = "errors/error#.html"
        )

        // Handle RequestValidationException for detailed validation errors (Video 6)
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to (cause.reasons.joinToString("; "))))
        }


        // Handle TooManyRequests(429) status code (Video 7)
        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            call.respondText(
                text = "Too Many Requests(429). Please try again after ${retryAfter ?: "some time"}.",
                status = HttpStatusCode.TooManyRequests
            )

        }
    }
}