package com.discode.backend.models.requests

data class PostMessageRequest(
    val chatMemberId: Long,
    val content: String,
)
