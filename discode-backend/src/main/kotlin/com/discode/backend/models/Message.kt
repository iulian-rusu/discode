package com.discode.backend.models

import java.util.*

data class Message(
    val author: ChatMember,
    val creationDate: Date,
    val content: String,
    val codeOutput: String?
)
