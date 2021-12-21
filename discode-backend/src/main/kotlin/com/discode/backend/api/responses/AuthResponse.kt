package com.discode.backend.api.responses

import com.discode.backend.business.models.User

data class AuthResponse(
    val token: String,
    val user: User
)
