package com.discode.backend.business.models

data class ChatMember(
    val chatMemberId: Long,
    val chatId: Long,
    val userId: Long,
    val status: String
)
