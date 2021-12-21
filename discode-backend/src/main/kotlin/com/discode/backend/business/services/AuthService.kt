package com.discode.backend.business.services

import com.discode.backend.api.requests.AuthRequest
import com.discode.backend.api.responses.AuthResponse
import com.discode.backend.business.interfaces.AuthServiceInterface
import com.discode.backend.business.security.Encoder
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.persistence.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService : JwtAuthorized(), AuthServiceInterface {
    @Autowired
    private lateinit var userRepository: UserRepository

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    override fun authenticate(request: AuthRequest): AuthResponse {
        val databaseHash = userRepository.findPasswordHash(request.username)
        return if (!Encoder.matches(request.password, databaseHash)) {
            logger.warn("Invalid password attempt for user ${request.username}")
            throw ResponseStatusException(HttpStatus.CONFLICT, "Invalid password")
        } else {
            val user = userRepository.findOne(request.username)
            AuthResponse(jwtProvider.createToken(user.userId), user)
        }
    }
}