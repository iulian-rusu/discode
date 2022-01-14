package com.discode.backend.api.responses

data class UserChatResponseEntry(
    val ownerId: Long,
    val chatId: Long,
    val chatName: String
)
