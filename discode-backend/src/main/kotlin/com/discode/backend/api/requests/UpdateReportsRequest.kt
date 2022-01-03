package com.discode.backend.api.requests

import com.discode.backend.business.models.ReportStatus

data class UpdateReportsRequest(
    val messageId: Long,
    val status: ReportStatus
)
