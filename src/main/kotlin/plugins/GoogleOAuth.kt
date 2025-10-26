package com.hamzehk.plugins

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.*
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.util.*

fun AuthenticationConfig.configureGoogleOAuth(httpClient: HttpClient) {

    oauth("google-oauth") {
        urlProvider = { "http://localhost:8080/callback" }
        providerLookup = {
            OAuthServerSettings.OAuth2ServerSettings(
                name = "google",
                authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                requestMethod = HttpMethod.Post,
                clientId = System.getenv("GOOGLE_CLIENT_ID"),
                clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                defaultScopes = listOf(
                    "https://www.googleapis.com/auth/userinfo.profile",
                    "https://www.googleapis.com/auth/userinfo.email"
                ),
                extraAuthParameters = listOf("access_type" to "offline")
            )
        }
        client = httpClient
    }

}


@Serializable
data class UserInfo(
    val userId: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String
)

@Serializable
data class GoogleUserResponse(
    val name: String,
    val email: String
)

suspend fun fetchGoogleUserInfo(httpClient: HttpClient, accessToken: String): UserInfo? {

    val response: HttpResponse = httpClient
        .get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }

    if (response.status == HttpStatusCode.OK) {
        val googleUser = response.body<GoogleUserResponse>()
        return UserInfo(
            name = googleUser.name,
            email = googleUser.email
        )
    }else{
        return null
    }

}