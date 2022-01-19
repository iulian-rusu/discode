package com.discode.backend.business.models

import java.util.*

data class Report(
    val messageId: Long,
    val message: String,
    val reported: UserIdentification,
    val reporter: UserIdentification,
    val date: Date,
    val reason: String?,
    val status: ReportStatus
)
