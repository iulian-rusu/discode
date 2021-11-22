package com.discode.backend.services

import com.discode.backend.security.jwt.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
abstract class AuthorizedService {
    @Autowired
    protected lateinit var jwtProvider: JwtProvider

    protected fun <T> ifAuthorized(userId: Long, header: String?, action: () -> ResponseEntity<T>): ResponseEntity<T> {
        val token = jwtProvider.getToken(header) ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        val details = jwtProvider.getUserDetails(token)
        if (details.userId != userId && !details.isAdmin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        return action()
    }
}