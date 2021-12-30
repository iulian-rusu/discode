package com.discode.backend.api.requests

data class BanUserRequest(
    val userId: Long,
    val seconds: Long,
    val banReason: String
)