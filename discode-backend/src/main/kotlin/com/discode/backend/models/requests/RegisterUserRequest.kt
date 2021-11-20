package com.discode.backend.models.requests

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)