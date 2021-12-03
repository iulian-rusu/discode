package com.discode.backend.models

import java.util.*

data class Message(
    val messageId: Long,
    val chatMemberId: Long,
    val creationData: Date,
    val content: String,
    val codeOutput: String
)
