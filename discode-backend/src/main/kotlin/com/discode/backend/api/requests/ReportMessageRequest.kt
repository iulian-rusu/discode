package com.discode.backend.api.requests

data class ReportMessageRequest(
    val messageId: Long,
    val reporterId: Long,
    val reportReason: String?
)
