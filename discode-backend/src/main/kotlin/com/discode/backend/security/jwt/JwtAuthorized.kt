package com.discode.backend.security.jwt

import com.discode.backend.controllers.ChatController
import com.discode.backend.security.SimpleUserDetails
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
abstract class JwtAuthorized {
    companion object {
        private fun unauthorized(): Nothing =
            throw JwtAuthenticationException(HttpStatus.UNAUTHORIZED, "Missing JWT")

        private fun forbidden(): Nothing =
            throw JwtAuthenticationException(HttpStatus.FORBIDDEN, "Not enough permissions")
    }

    @Autowired
    protected lateinit var jwtProvider: JwtProvider

    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    protected fun <T> authorizedScope(action: () -> T): T {
        return try {
            action()
        } catch (e: ResponseStatusException) {
            logger.error("[ResponseStatusException]: $e")
            throw e
        } catch (e: JwtAuthenticationException) {
            throw ResponseStatusException(e.status, e.message)
        }
    }

    protected fun <T> ifAuthorizedOn(userId: Long, header: String?, action: () -> T): T {
        val token = jwtProvider.getToken(header) ?: unauthorized()
        val details = jwtProvider.getUserDetails(token)
        if (details.userId == userId || details.isAdmin)
            return action()
        forbidden()
    }

    protected fun <T> ifAuthorized(header: String?, authorizer: (SimpleUserDetails) -> Boolean, action: () -> T): T {
        val token = jwtProvider.getToken(header) ?: unauthorized()
        val details = jwtProvider.getUserDetails(token)
        if (authorizer(details))
            return action()
        forbidden()
    }
}