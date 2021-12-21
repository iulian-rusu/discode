package com.discode.backend.business.security.jwt

import com.discode.backend.business.security.SimpleUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
abstract class JwtAuthorized {
    companion object {
        private fun throwUnauthorized(): Nothing =
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing JWT")

        private fun throwForbidden(): Nothing =
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough permissions")
    }

    @Autowired
    protected lateinit var jwtProvider: JwtProvider

    protected fun <T> ifAuthorizedAs(userId: Long, header: String?, action: () -> T): T {
        val token = jwtProvider.getToken(header) ?: throwUnauthorized()
        val details = jwtProvider.getUserDetails(token)
        if (details.userId == userId)
            return action()
        throwForbidden()
    }

    protected fun <T> ifAuthorized(header: String?, authorizer: (SimpleUserDetails) -> Boolean, action: () -> T): T {
        val token = jwtProvider.getToken(header) ?: throwUnauthorized()
        val details = jwtProvider.getUserDetails(token)
        if (authorizer(details))
            return action()
        throwForbidden()
    }
}