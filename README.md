# 🧩 Ktor Server Fundamentals Project

This project demonstrates the core features of **Ktor**, a Kotlin-based asynchronous web framework.  
It covers how to build secure and scalable backend services using authentication, plugins, and real-time communication.

---

## 🚀 Features Implemented

| Category | Description |
|-----------|-------------|
| **Static Content** | Served static files and resources using `staticResources` and `staticFiles`. |
| **Authentication** | Implemented Basic, Bearer, Session, and JWT authentication flows. |
| **Google OAuth** | Integrated Google Sign-In and generated JWT tokens for secure access. |
| **Server-Sent Events (SSE)** | Streamed real-time events to clients using SSE. |
| **WebSockets** | Created a multi-user chat system supporting broadcast and private messaging. |
| **Rate Limiting** | Restricted API calls to control request frequency. |
| **Custom Plugins** | Built a reusable plugin that adds custom headers to all responses. |
| **Call Logging & Shutdown URL** | Configured request logging and graceful server shutdown. |

---

## 🧠 Tech Stack

- **Language:** Kotlin  
- **Framework:** Ktor  
- **Build Tool:** Gradle (Kotlin DSL)  
- **Testing:** Postman & WebSocket workspace  

---

## ⚙️ How to Run

```bash
# Clone the repo
git clone https://github.com/hamzehabuhijleh/ktor-server-fundamentals.git
cd ktor-server-fundamentals

# Build and run
./gradlew build
./gradlew run
