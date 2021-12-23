package com.discode.backend.api.requests

data class PostMessageRequest(
    val userId: Long,
    val content: String,
)
