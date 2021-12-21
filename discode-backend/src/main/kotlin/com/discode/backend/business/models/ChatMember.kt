package com.discode.backend.business.models

data class ChatMember(
    val chatId: Long,
    val userId: Long,
    val status: Char
)
