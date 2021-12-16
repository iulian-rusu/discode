package com.discode.backend.controllers

import com.discode.backend.interfaces.AuthServiceInterface
import com.discode.backend.models.requests.AuthRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController {
    @Autowired
    private lateinit var authService: AuthServiceInterface

    @PostMapping("/auth")
    fun authenticate(@RequestBody(required = true) request: AuthRequest) = authService.authenticate(request)
}