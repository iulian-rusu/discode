package com.discode.backend.api.utils

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object HttpResponse {
    inline fun <reified T> created(obj: T): ResponseEntity<T> = ResponseEntity.status(HttpStatus.CREATED).body(obj)

    const val GENERIC_ERROR_MESSAGE = "Congratulations, you broke the server with your api request 😡. " +
            "It is (most likely) because I forgot to check some corner case on the server side. " +
            "It might also happen if you are sending request at 3:00 in the morning while being sleep deprived" +
            " and having enough caffeine in your body to supply a starbucks store. " +
            "Either way I have no idea what you are trying to do so I'm giving you this useless message."
}