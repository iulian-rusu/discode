package com.discode.backend.api.requests

import com.discode.backend.business.models.ChatMemberStatus

data class UpdateChatMemberRequest(
    val status: ChatMemberStatus
)