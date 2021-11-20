package com.discode.backend.models.responses

import com.discode.backend.models.User

data class AuthUserResponse(
    val userId: Long,
    val user: User
)
