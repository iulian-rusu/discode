package com.discode.backend.models.requests

data class PostMessageRequest(
    val userId: Long,
    val content: String,
)
