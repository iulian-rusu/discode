package com.discode.backend.business.models

import java.util.*

data class Message(
    val messageId: Long,
    val author: ChatMember,
    val creationDate: Date,
    val content: String,
    var codeOutput: String?
)
