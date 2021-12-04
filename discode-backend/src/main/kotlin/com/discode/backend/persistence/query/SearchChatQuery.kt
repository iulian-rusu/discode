package com.discode.backend.persistence.query

class SearchChatQuery(userId: Long, queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    init {
        params["userId"] = userId
    }

    override fun getSql() = """
        SELECT * FROM chats c
        WHERE c.chat_id IN (SELECT chat_id FROM chat_members cm WHERE cm.user_id = :userId)
        LIMIT :itemsPerPage OFFSET :offset 
    """.trimIndent()
}