package com.discode.backend.persistence.query

import com.discode.backend.api.requests.UpdateChatMemberRequest
import com.discode.backend.business.models.ChatMemberStatus

class UpdateChatMemberQuery(
    val chatId: Long,
    val userId: Long,
    val status: ChatMemberStatus
) : ParametrizedQuery() {
    constructor(chatId: Long, userId: Long, request: UpdateChatMemberRequest) : this(chatId, userId, request.status)

    init {
        params["chatId"] = chatId
        params["userId"] = userId
        params["status"] = status.toString()
    }

    override fun getSql() = "UPDATE chat_members SET status = :status WHERE user_id = :userId AND chat_id = :chatId"
}