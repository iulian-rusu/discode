package com.discode.backend.controllers

import com.discode.backend.interfaces.AuthServiceInterface
import com.discode.backend.models.requests.AuthRequest
import com.discode.backend.models.responses.AuthResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class AuthController {
    @Autowired
    private lateinit var authService: AuthServiceInterface

    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/auth")
    fun authenticate(@RequestBody(required = true) request: AuthRequest): ResponseEntity<AuthResponse> {
        return try {
            ResponseEntity.ok(authService.authenticate(request))
        } catch (e: ResponseStatusException) {
            throw e
        } catch (e: Exception) {
            logger.error("GET /api/auth: $e")
            throw ResponseStatusException(HttpStatus.CONFLICT, "Invalid credentials")
        }
    }
}