package com.discode.backend.models.responses

import com.discode.backend.models.User

data class AuthResponse(
    val token: String,
    val user: User
)
