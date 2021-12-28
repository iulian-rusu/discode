package com.discode.backend.api.requests

data class PostUserBanRequest(
    val userId: Long,
    val seconds: Long,
    val banReason: String
)