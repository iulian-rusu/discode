package com.discode.backend.business.models

import java.util.*

data class UserBan(
    val banId: Long,
    val userId: Long,
    val startDate: Date,
    val endDate: Date,
    val banReason: String
)
