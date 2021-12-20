package com.discode.backend.controllers.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object HttpResponse {
    inline fun <reified T> created(obj: T): ResponseEntity<T> = ResponseEntity.status(HttpStatus.CREATED).body(obj)
}