package com.discode.backend.controllers

import com.discode.backend.interfaces.AuthServiceInterface
import com.discode.backend.models.requests.AuthRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.utils.ScopeGuarded
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class AuthController : ScopeGuarded() {
    @Autowired
    private lateinit var authService: AuthServiceInterface


    @PostMapping("/auth")
    fun authenticate(@RequestBody(required = true) request: AuthRequest): ResponseEntity<AuthResponse> {
        return guardedWith(HttpStatus.CONFLICT, "Invalid credentials") {
            ResponseEntity.ok(authService.authenticate(request))
        }
    }
}