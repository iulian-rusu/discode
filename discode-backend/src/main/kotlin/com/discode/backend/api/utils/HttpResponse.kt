package com.discode.backend.api.utils

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object HttpResponse {
    inline fun <reified T> created(obj: T): ResponseEntity<T> = ResponseEntity.status(HttpStatus.CREATED).body(obj)

    const val GENERIC_ERROR_MESSAGE = "You broke the server with your api request 😡"
}