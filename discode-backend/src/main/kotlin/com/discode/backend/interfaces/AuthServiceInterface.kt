package com.discode.backend.interfaces

import com.discode.backend.models.requests.AuthRequest
import com.discode.backend.models.responses.AuthResponse
import org.springframework.http.ResponseEntity

interface AuthServiceInterface {
    fun authenticate(request: AuthRequest): ResponseEntity<AuthResponse>
}