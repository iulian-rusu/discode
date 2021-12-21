package com.discode.backend.persistence.query

import com.discode.backend.api.requests.UpdateChatMemberRequest

class UpdateChatMemberQuery(
    val chatId: Long,
    val userId: Long,
    status: Char
) : ParametrizedQuery() {
    constructor(chatId: Long, userId: Long, request: UpdateChatMemberRequest) : this(chatId, userId, request.status)

    init {
        params["chatId"] = chatId
        params["userId"] = userId
        params["status"] = status
    }

    override fun getSql() = "UPDATE chat_members SET status = :status WHERE user_id = :userId AND chat_id = :chatId"
}