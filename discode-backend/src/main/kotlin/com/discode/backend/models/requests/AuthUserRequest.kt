package com.discode.backend.models.requests

data class AuthUserRequest(
    val username: String,
    val password: String
)