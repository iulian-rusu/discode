package com.discode.backend.business.models

import java.util.*

data class Report(
    val messageId: Long,
    val reported: UserIdentification,
    val reporter: UserIdentification,
    val date: Date,
    val reason: String?,
    val status: ReportStatus
)
