package com.discode.backend.controllers

import com.discode.backend.models.UserCredentials
import com.discode.backend.responses.AuthenticationResponse
import com.discode.backend.responses.RegistrationResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AccessController {
    @PostMapping("/auth")
    fun authenticate(@RequestBody(required = true) credentials: UserCredentials) =
        ResponseEntity.status(HttpStatus.CONFLICT).build<AuthenticationResponse>()

    @PostMapping("/register")
    fun register(@RequestBody(required = true) credentials: UserCredentials) =
        ResponseEntity.status(HttpStatus.CONFLICT).build<RegistrationResponse>()
}