package com.discode.backend.models.requests

data class CreateChatRequest(
    val ownerId: Long,
    val chatName: String
)
