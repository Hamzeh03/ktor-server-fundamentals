package com.hamzehk.plugins

import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.resources.Resource
import io.ktor.server.application.*
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.*
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.sse.sse
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.sse.ServerSentEvent
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.ConcurrentHashMap


fun Application.configureRouting(config: JWTConfig , httpClient : HttpClient) {


    // In-memory user database for demonstration purposes Video 13 and Video 14
    val userDb = mutableMapOf<String, String>()

    // In-memory user data storage for OAuth Video 15
    val userDataBase = mutableMapOf<String, UserInfo>()

    // In-memory user data storage for WebSocket Video 17
    val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()


    routing {

        /*
        // Protected route with Basic Authentication Video 10
        // Username: admin, Password: password
       authenticate("basic-auth") {
           get("/") {

               val principal = call.principal<UserIdPrincipal>()
               val username = principal?.name ?: "Unknown user"

               call.respondText("Hello, $username! You are authenticated.")
           }
       }
         */

        /*
        // Protected route with Bearer Authentication Video 12
        // Token: token1, token2, token3, token4
        authenticate("bearer-auth") {
            get("/") {

                val principal = call.principal<UserIdPrincipal>()
                val username = principal?.name ?: "Unknown user"

                call.respondText("Hello, $username! You are authenticated.")
            }
        }
         */

        /*
        // Protected route with Session Authentication Video 13
        post ("signup") {
            val requestData = call.receive<AuthRequest>()
            if (userDb.containsKey(requestData.username)) {
                call.respondText("Username already exists. Please choose a different username.")
            } else {
                userDb[requestData.username] = requestData.password
                call.sessions.set(UserSession(requestData.username))
                call.respondText("User ${requestData.username} signed up successfully.")
            }
        }
        post ("login") {
            val requestData = call.receive<AuthRequest>()

            val storedPassword = userDb[requestData.username]

            if (storedPassword == null || storedPassword != requestData.password) {
                call.respondText("Invalid username or password.")
            } else {
                call.sessions.set(UserSession(requestData.username))
                call.respondText("User ${requestData.username} logged in successfully.")
            }
        }
        post ("logout") {
            val userSession = call.sessions.get<UserSession>()
            if (userSession != null) {
                call.sessions.clear<UserSession>()
                call.respondText("User ${userSession.username} logged out successfully.")
            } else {
                call.respondText("No active session found.")
            }
        }
        authenticate("session-auth") {
            get("/") {
                val username = call.principal<UserSession>()?.username
                call.respondText("Hello, $username! You are authenticated.")
            }
        }
        */

        /*
        // Protected route with JWT Authentication Video 14
        post ("signup") {
            val requestData = call.receive<AuthRequest>()
            if (userDb.containsKey(requestData.username)) {
                call.respondText("Username already exists. Please choose a different username.")
            } else {
                userDb[requestData.username] = requestData.password
                val token = generateToken(config = config, username = requestData.username)
                call.respond(mapOf("token" to token) )
            }
        }

        post ("login") {
            val requestData = call.receive<AuthRequest>()

            val storedPassword = userDb[requestData.username]
                ?: return@post call.respondText("Invalid username or password.")

            if (storedPassword == requestData.password) {
                val token = generateToken(config = config, username = requestData.username)
                call.respond(mapOf("token" to token) )
            } else {
                call.respondText("Invalid credentials.")
            }
        }

        authenticate("jwt-auth") {
            get("/") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString()
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())

                call.respondText("Hello, $username! The token expire after $expiresAt ms.")
            }
        }
         */

        /*
        // Protected route with OAuth Authentication Video 15
        authenticate("google-oauth") {
            get("login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("callback") {
                val principal : OAuthAccessTokenResponse.OAuth2? = call.principal()
                if (principal == null) {
                    call.respondText("Authentication failed OAuth" , status = HttpStatusCode.Unauthorized)
                    return@get
                } else {
                    val  accessToken = principal.accessToken
                    val userInfo = fetchGoogleUserInfo(httpClient = httpClient, accessToken = accessToken)

                    if (userInfo != null) {
                        userDataBase[userInfo.userId] = userInfo
                        val token = generateToken(config = config, username = userInfo.userId)
                        call.respond("token" to token )
                    }else{
                        call.respond(HttpStatusCode.InternalServerError)
                    }

                }
            }
        }

        authenticate("jwt-auth") {
            get("/") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal?.payload?.getClaim("username")?.asString()
                val userInfo = userDataBase[username] ?: mapOf("error" to true)
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())

                call.respond(userInfo)
            }
        }
        */

        /*
        // SSE Video 16
        sse("events"){
            repeat(6){
                send(ServerSentEvent("Hamzeh Events : ${it + 1}"))
                delay(1000L)
            }
        }
         */

        /*
        // Web Socket Video 17
        webSocket("chat"){
            val username = call.request.queryParameters["username"] ?: run {
                this.close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "username is required for establishing connect"))
                return@webSocket
            }

            onlineUsers[username] = this

            send("You are connected!!")
            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text){
                        val message = Json.decodeFromString<Message>(frame.readText())
                        if( message.to.isNullOrBlank()){
                            onlineUsers.values.forEach {
                                it.send("$username : ${message.text}")
                            }
                        }else{
                            val session = onlineUsers[message.to]

                            session?.send("$username : ${message.text}")
                        }
                    }
                }
            }finally {
                onlineUsers.remove(username)
                this.close()
            }
        }
         */

        /*
        // Call Logging Video 19
        get("hello"){
            call.respondText("Hello world I am hamzeh abu hijleh ")
        }
        get("hi"){
            call.respondText("hi world I am hamzeh abu hijleh ")
        }
         */

        /*
        //Custom Plugin Video 20
        get("hello"){
            call.respondText("Hello world I am hamzeh abu hijleh ")
        }
        get("hi"){
            call.respondText("hi world I am hamzeh abu hijleh ")
        }
         */


    }
}

// Session data class for Session Authentication Video 13 and Video 14
@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)

// for WebSockets Video 17
@Serializable
data class Message(
    val text: String,
    val to: String? = null
)

///////////////////////////////////////////////////////////////////////////////////////////////////

//Video 2
fun Route.dynamicRoutes() {

    get("/") {
        call.respondText("Hello World!")
    }

    // Example: /blogs/123?q1=hello&q2=world
    get("/blogs/{id}") {
        val id = call.pathParameters["id"]
        val q1 = call.queryParameters["q1"]
        val q2 = call.queryParameters["q2"]

        call.respondText("Blog ID: $id, Query One: $q1, Query Two: $q2")
    }

    // This route will match any path that ends with /test
    get(Regex(".+/test")) {
        call.respondText("Regex route matched!")
    }

    //api/v1/users
    //api/v2/users
    //api/v3/users
    get(Regex("api/(?<apiVersion>v[1-3])/users")) {
        val apiVersion = call.parameters["apiVersion"]
        call.respondText("API Version: $apiVersion")
    }

    // Example: /blogs?sort=New
    get<Blogs> { blogs ->
        val sort = blogs.sort
        call.respondText("Blogs sorted by: $sort")
    }

    // Example: /blogs/{id}?sort=New
    delete<Blogs.Blog> { blog ->
        val sort = blog.parent.sort
        val blogId = blog.id
        call.respondText("Deleted blog with ID: $blogId from blogs sorted by: $sort")
    }
}

//Video 2
fun Route.accountRoutes() {
    route("accounts") {
        // accounts/users/{userId} creating
        // accounts/users/{userId} deleting
        route("users") {
            get("{id}") { }
            post("") { }
            patch("{id}") { }
        }

        // accounts/auth/login
        // accounts/auth/signup
        route("auth") {
            post("login") { }
            post("signup") { }
        }
    }
}

//Video 3
fun Route.readFilesAndJsonRoutes() {
    post("/greet") {
        val name = call.receiveText()
        call.respondText("Hello, $name!")
    }

    post("channel") {
        val channel = call.receiveChannel()
        val text = channel.readRemaining().readText()
        call.respondText("Received via channel: $text")
    }

    post("upload") {
        val file = File("uploads/sample2.jpg").apply {
            parentFile?.mkdirs()
        }
//            /*
//            val byteArray = call.receive<ByteArray>()
//            file.writeBytes(byteArray)
//             */
//            val stream = call.receiveStream()
//            FileOutputStream(file).use { outputStream ->
//                stream.copyTo(outputStream, bufferSize = 16 * 1024)
//            }

        val channel = call.receiveChannel()
        channel.copyAndClose(file.writeChannel())

        call.respondText("File uploaded successfully: ${file.absolutePath}")
    }

    post("/products") {
        val product = call.receiveNullable<Product>() ?: return@post call.respond(
            HttpStatusCode.BadRequest, "Invalid product data"
        )

        call.respond(product)
    }
}

//Video 4
fun Route.EncodedAndMultipartFormRoutes() {
    post("checkout") {
        val formData = call.receiveParameters()
        val productId = formData["productId"]
        val quantity = formData["quantity"]

        call.respondText("Received checkout for product ID: $productId with quantity: $quantity")
    }

    post("multipleData") {
        val data = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 50)//50 MB

        val fields = mutableMapOf<String, MutableList<String>>()

        data.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    val key = part.name ?: return@forEachPart

                    fields.getOrPut(key) { mutableListOf() }.add(part.value)
                    part.dispose()
                }

                is PartData.FileItem -> {
                    val key = part.name ?: return@forEachPart
                    val fileName = part.originalFileName ?: "unknown"

                    fields.getOrPut(key) { mutableListOf() }.add(fileName)

                    val file = File("uploads/$fileName").apply { parentFile?.mkdirs() }

                    part.provider().copyAndClose(file.writeChannel())
                    part.dispose()
                }

                else -> {}
            }
        }

        call.respond("From Field $fields")
    }
}

//Video 5
fun Route.statusPagesRoutes() {

    post("product") {
        throw kotlin.Exception("Something went wrong while creating product")
    }
    post("product400") {
        call.respond(HttpStatusCode.BadRequest)
    }
    post("product401") {
        call.respond(HttpStatusCode.Unauthorized)
    }
    post("product403") {
        call.respond(HttpStatusCode.Forbidden)
    }
    post("product404") {
        call.respond(HttpStatusCode.NotFound)
    }
}

//Video 6
fun Route.RequestValidationRoutes() {
    // Just for testing Request Validation Of String and Product2 (Object)
    post("message1") {
        val message = call.receive<String>()
        call.respondText("Received message: $message")
    }
    post("message2") {
        val message = call.receive<String>()
        call.respondText("Received message: $message")
    }
    //Object
    post("product2") {
        val product = call.receive<Product>()
        call.respond(product)
    }
}

//Video 7
fun Route.RateLimitRoutes() {

    post("hello") {
        val requestLeft = call.response.headers["X-RateLimit-Remaining"]
        call.respondText("Hello from Ktor!, you still have $requestLeft requests left")
    }

    post("hello1") {
        val requestLeft = call.response.headers["X-RateLimit-Remaining"]
        call.respondText("Hello from Ktor!, you still have $requestLeft requests left")
    }

    rateLimit(RateLimitName("public")) {
        post("public") {
            val requestLeft = call.response.headers["X-RateLimit-Remaining"]
            call.respondText("Hello from Ktor!, you still have $requestLeft requests left")
        }
    }

    rateLimit(RateLimitName("protected")) {
        post("protected") {
            val requestLeft = call.response.headers["X-RateLimit-Remaining"]
            call.respondText("Hello from Ktor!, you still have $requestLeft requests left")
        }
    }
}

//Video 9
fun Route.ServingContentsRoutes() {
    // Serve static resources from the "static" folder in the resources directory
    // EX : http://localhost:8080/profile to access static/profile.html
    staticResources("", "/static") {
        extensions("html")
    }

    // Serve static files from the "uploads" directory on the file system
    // EX : http://localhost:8080/uploads/sample.jpg to access uploads/sample.jpg
    staticFiles("uploads", File("uploads")) {
        // You can also add filters to include or exclude specific files
        exclude { file ->
            file.path.contains("Me")
        }

        // You can set custom content types for specific files if needed
        // EX : http://localhost:8080/uploads/sample2.txt show a jpeg image not a text file
        contentType { file ->
            when (file.name) {
                "sample2.txt" -> ContentType.Image.JPEG
                else -> null
            }
        }
    }
}

// Define resource classes (accountRoutes uses these)
// Example: /blogs/{id}?sort=New
@Resource("blogs")
class Blogs(val sort: String? = "New") {
    @Resource("{id}")
    data class Blog(val parent: Blogs = Blogs(), val id: String)
}

// Define a data class for Product (readFilesAndJsonRoutes uses this) Video 3 and 6
@Serializable
data class Product(
    val name: String?, val category: String?, val price: Double?
)