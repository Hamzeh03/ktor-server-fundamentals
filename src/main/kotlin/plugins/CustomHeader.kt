package com.hamzehk.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.*

fun Application.configureCustomHeader() {
    install(CustomHeader) {
        headerKey = "X-Powered-By"
        headerValue = "Ktor Custom Plugin"
    }
}

// Custom Plugin Video 20
class CustomHeader(configuration: Configuration) {

    private val headerKey = configuration.headerKey
    private val headerValue = configuration.headerValue

    class Configuration {
        var headerKey: String = "X-Custom_Header"
        var headerValue: String = "Default Value"
    }

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, Configuration, CustomHeader> {
        override val key: AttributeKey<CustomHeader> = AttributeKey("CustomHeader")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): CustomHeader {
            val configuration = Configuration().apply(configure)
            val plugin = CustomHeader(configuration)

            pipeline.intercept(ApplicationCallPipeline.Plugins) {
                call.response.header(plugin.headerKey, plugin.headerValue)
            }

            return plugin
        }
    }
}