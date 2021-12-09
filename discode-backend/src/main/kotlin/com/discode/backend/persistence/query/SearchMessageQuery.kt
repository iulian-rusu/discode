package com.discode.backend.persistence.query

class SearchMessageQuery(val chatId: Long, queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    init {
        params["chatId"] = chatId
    }

    override fun getSql() = """
        SELECT * FROM messages m INNER JOIN chat_members cm USING (chat_member_id)
        WHERE cm.chat_id = :chatId
        ORDER BY m.message_id DESC  
        LIMIT :itemsPerPage OFFSET :offset
    """.trimIndent()
}