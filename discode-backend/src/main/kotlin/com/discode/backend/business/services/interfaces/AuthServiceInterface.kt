package com.discode.backend.business.services.interfaces

import com.discode.backend.api.requests.AuthRequest
import com.discode.backend.api.responses.AuthResponse

interface AuthServiceInterface {
    fun authenticate(request: AuthRequest): AuthResponse
}