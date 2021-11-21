package com.discode.backend.models.requests

data class AuthRequest(
    val username: String,
    val password: String
)