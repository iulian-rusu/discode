package com.discode.backend.controllers

import com.discode.backend.models.UserCredentials
import com.discode.backend.models.AuthResponse
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
    fun authenticate(@RequestBody(required = true) credentials: UserCredentials) =
        ResponseEntity.status(HttpStatus.CONFLICT).build<AuthResponse>()
}