package com.discode.backend.models

data class AuthResponse(
    val userId: Long,
    val user: User
)
