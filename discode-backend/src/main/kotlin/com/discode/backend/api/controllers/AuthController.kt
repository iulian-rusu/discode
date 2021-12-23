package com.discode.backend.api.controllers

import com.discode.backend.api.requests.AuthRequest
import com.discode.backend.api.responses.AuthResponse
import com.discode.backend.api.utils.ScopeGuarded
import com.discode.backend.business.interfaces.AuthServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class AuthController : ScopeGuarded(AuthController::class) {
    @Autowired
    private lateinit var authService: AuthServiceInterface


    @PostMapping("/auth")
    fun authenticate(@RequestBody(required = true) request: AuthRequest): ResponseEntity<AuthResponse> {
        return guardedWith(HttpStatus.CONFLICT, "Invalid credentials") {
            ResponseEntity.ok(authService.authenticate(request))
        }
    }
}