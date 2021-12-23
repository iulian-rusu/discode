package com.discode.backend.business.models

enum class ReportStatus(val code: String) {
    PENDING("p"),
    REVIEWED("r");

    companion object {
        fun contains(element: String) = ChatMemberStatus.values().any { it.code == element }
    }
}