package com.hamzehk.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {

    // Custom Request Validation Rules (Video 6)
    install(RequestValidation) {

        validate<String> { body ->
            if (body.isBlank()) ValidationResult.Invalid("Request body cannot be blank")
            else if (body.startsWith("hello")) ValidationResult.Invalid("Request body cannot start with 'hello'")
            else ValidationResult.Valid
        }

        validate<Product> { body ->

            if (body.name.isNullOrBlank())
                ValidationResult.Invalid("Product Name cannot be blank")
            else if (body.category.isNullOrBlank())
                ValidationResult.Invalid("Product Category cannot be blank")
            else if (body.price == null || body.price <= 0)
                ValidationResult.Invalid("Product Price must be greater than zero")
            else ValidationResult.Valid

        }
    }
}