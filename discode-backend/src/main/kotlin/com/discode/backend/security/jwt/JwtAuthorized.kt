package com.discode.backend.security.jwt

import com.discode.backend.security.SimpleUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
abstract class JwtAuthorized {
    @Autowired
    protected lateinit var jwtProvider: JwtProvider

    protected fun <T> ifAuthorized(userId: Long, header: String?, action: () -> ResponseEntity<T>): ResponseEntity<T> {
        val token = jwtProvider.getToken(header) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val details = jwtProvider.getUserDetails(token)
        if (details.userId == userId || details.isAdmin)
            return action()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    protected fun <T> ifAuthorized(
        userId: Long,
        header: String?,
        authorizer: (Long, SimpleUserDetails) -> Boolean,
        action: () -> ResponseEntity<T>
    ): ResponseEntity<T> {
        val token = jwtProvider.getToken(header) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val details = jwtProvider.getUserDetails(token)
        if (authorizer(userId, details))
            return action()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}