package com.discode.backend.models

data class ChatMember(
    val chatId: Long,
    val userId: Long,
    val status: Char
)
