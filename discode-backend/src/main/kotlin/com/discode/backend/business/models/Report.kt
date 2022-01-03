package com.discode.backend.business.models

import java.util.*

data class Report(
    val messageId: Long,
    val reporterId: Long,
    val reportDate: Date,
    val reportReason: String?,
    val status: ReportStatus
)
