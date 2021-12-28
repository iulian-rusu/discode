package com.discode.backend.api.requests

data class PostReportRequest(
    val messageId: Long,
    val reporterId: Long,
    val reportReason: String?
)
