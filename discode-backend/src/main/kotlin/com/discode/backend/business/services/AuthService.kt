package com.discode.backend.business.services

import com.discode.backend.api.requests.AuthRequest
import com.discode.backend.api.responses.AuthResponse
import com.discode.backend.business.models.UserBan
import com.discode.backend.business.security.Encoder
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.business.services.interfaces.AuthServiceInterface
import com.discode.backend.persistence.UserBanRepository
import com.discode.backend.persistence.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class AuthService : JwtAuthorized(), AuthServiceInterface {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userBanRepository: UserBanRepository

    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    override fun authenticate(request: AuthRequest): AuthResponse {
        val databaseHash = userRepository.findPasswordHash(request.username)
        return if (!Encoder.matches(request.password, databaseHash)) {
            logger.warn("Invalid password attempt for user ${request.username}")
            throw ResponseStatusException(HttpStatus.CONFLICT, "Invalid password")
        } else {
            val user = userRepository.findOne(request.username)
            val latestUserBan = userBanRepository.findBansForUser(user.userId).firstOrNull()

            logger.info(Date().time.toString())
            if (latestUserBan != null)
                logger.info(latestUserBan.endDate.time.toString())

            if (latestUserBan != null && latestUserBan.endDate.time > Date().time) {
                handleBannedUser(latestUserBan)
            } else {
                AuthResponse(jwtProvider.createToken(user.userId), user)
            }
        }
    }

    private fun handleBannedUser(ban: UserBan): Nothing {
        throw ResponseStatusException(
            HttpStatus.FORBIDDEN,
            "User is banned from ${ban.startDate} until ${ban.endDate}. Reason: ${ban.reason}"
        )
    }
}