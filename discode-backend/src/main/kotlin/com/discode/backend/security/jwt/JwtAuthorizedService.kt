package com.discode.backend.security.jwt

import com.discode.backend.security.SimpleUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
abstract class JwtAuthorizedService {
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
        header: String?,
        authorizer: (SimpleUserDetails) -> Boolean,
        action: () -> ResponseEntity<T>
    ): ResponseEntity<T> {
        val token = jwtProvider.getToken(header) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val details = jwtProvider.getUserDetails(token)
        if (authorizer(details))
            return action()
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }

    protected fun <T> withUserId(header: String?, action: (Long) -> ResponseEntity<T>): ResponseEntity<T> {
        if (header == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val token = jwtProvider.getToken(header)!!
        val userId = jwtProvider.getUserId(token)
        return action(userId)
    }
}