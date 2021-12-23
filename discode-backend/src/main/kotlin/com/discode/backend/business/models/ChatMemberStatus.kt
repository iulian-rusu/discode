package com.discode.backend.business.models

enum class ChatMemberStatus(val code: String) {
    OWNER("o"),
    GUEST("g"),
    LEFT("l");

    companion object {
        fun contains(element: String) = values().any { it.code == element }
    }
}