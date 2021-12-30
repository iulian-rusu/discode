package com.discode.backend.api.requests

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)