package com.discode.backend.models

data class RegistrationResponse(
    val userId: Long,
    val user: User
)