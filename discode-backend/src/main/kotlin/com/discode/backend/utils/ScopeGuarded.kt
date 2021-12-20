package com.discode.backend.utils

import com.discode.backend.controllers.ChatController
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

abstract class ScopeGuarded {
    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    protected fun <T> guardedWith(status: HttpStatus, message: String, action: () -> T): T {
        return try {
            action()
        } catch (e: ResponseStatusException) {
            logger.error("Response status exception: $e")
            throw e
        } catch (e: Exception) {
            logger.error("Scope error: $e")
            throw ResponseStatusException(status, message)
        }
    }
}