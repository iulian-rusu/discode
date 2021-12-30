package com.discode.backend.api.requests

data class CreateChatRequest(
    val ownerId: Long,
    val chatName: String
)
