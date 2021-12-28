package com.discode.backend.api.requests

data class UpdateReportsRequest(
    val messageId: Long,
    val status: String
)
