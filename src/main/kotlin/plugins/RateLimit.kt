package com.hamzehk.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimit() {

    // Configure Rate Limiting (Video 7)
    install(RateLimit){

//        global {
//            rateLimiter (limit = 5, refillPeriod = 60.seconds)
//        }

        // Simple rate limiting
        register(RateLimitName("public")){
            rateLimiter (limit = 10, refillPeriod = 60.seconds)
        }

        // Custom rate limiting based on request parameters
        register(RateLimitName("protected")){
            rateLimiter (limit = 10, refillPeriod = 60.seconds)
            requestKey { call ->
                call.request.queryParameters["type"] ?: "anonymous"
            }
            // Assign different weights based on the "type" parameter
            requestWeight { call , key ->
                when (key) {
                    "admin" -> 2
                    else -> 1
                }
            }
        }
    }
}