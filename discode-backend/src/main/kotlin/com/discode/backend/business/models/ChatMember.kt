package com.discode.backend.business.models

data class ChatMember(
    val chatMemberId: Long,
    val chatId: Long,
    val userId: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val imagePath: String?,
    val status: String
)
