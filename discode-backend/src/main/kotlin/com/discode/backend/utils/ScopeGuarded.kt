package com.discode.backend.utils

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.reflect.KClass

abstract class  ScopeGuarded(guardedType: KClass<*>) {
    private val logger = LoggerFactory.getLogger(guardedType.java)

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