package com.discode.backend.controllers

import com.discode.backend.models.requests.AuthRequest
import com.discode.backend.models.responses.AuthResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController {
    @PostMapping("/auth")
    fun authenticate(@RequestBody(required = true) request: AuthRequest) =
        ResponseEntity.status(HttpStatus.CONFLICT).build<AuthResponse>()
}