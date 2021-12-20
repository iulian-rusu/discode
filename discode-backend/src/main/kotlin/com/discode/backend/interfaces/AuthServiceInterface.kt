package com.discode.backend.interfaces

import com.discode.backend.models.requests.AuthRequest
import com.discode.backend.models.responses.AuthResponse

interface AuthServiceInterface {
    fun authenticate(request: AuthRequest): AuthResponse
}