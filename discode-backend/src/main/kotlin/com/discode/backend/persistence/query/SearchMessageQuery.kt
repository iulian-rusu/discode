package com.discode.backend.persistence.query

class SearchMessageQuery(chatId: Long, queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    init {
        params["chatId"] = chatId
    }

    override fun getSql() = """
        SELECT * FROM messages m INNER JOIN chat_members cm USING (chat_member_id)
        WHERE cm.chat_id = :chatId
        LIMIT :itemsPerPage OFFSET :offset
    """.trimIndent()
}