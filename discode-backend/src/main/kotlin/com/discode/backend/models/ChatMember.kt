package com.discode.backend.models

data class ChatMember(
    val chatMemberId: Long,
    val chatId: Long,
    val userId: Long,
    val status: Char
)
