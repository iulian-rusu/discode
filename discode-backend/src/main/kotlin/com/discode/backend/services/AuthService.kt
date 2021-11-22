package com.discode.backend.services

import com.discode.backend.interfaces.AuthInterface
import com.discode.backend.models.requests.AuthRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.UserRepository
import com.discode.backend.security.Encoder
import com.discode.backend.security.jwt.JwtAuthorized
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthService : JwtAuthorized(), AuthInterface {
    @Autowired
    private lateinit var userRepository: UserRepository

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    override fun authenticate(request: AuthRequest): ResponseEntity<AuthResponse> {
        return try {
            val databaseHash = userRepository.findPasswordHash(request.username)
            return if (!Encoder.matches(request.password, databaseHash)) {
                logger.warn("Invalid password attempt for user ${request.username}")
                ResponseEntity.status(HttpStatus.CONFLICT).build()
            } else {
                val user = userRepository.findOne(request.username)
                val response = AuthResponse(jwtProvider.createToken(user.userId), user)
                ResponseEntity.ok(response)
            }
        } catch (e: Exception) {
            logger.error("authenticate($request): $e")
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }
}