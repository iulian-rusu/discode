package com.discode.backend.business.models

import java.util.*

data class UserBan(
    val banId: Long,
    val user: UserIdentification,
    val startDate: Date,
    val endDate: Date,
    val reason: String
)
