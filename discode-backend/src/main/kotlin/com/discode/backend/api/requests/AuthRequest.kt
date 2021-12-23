package com.discode.backend.api.requests

data class AuthRequest(
    val username: String,
    val password: String
)