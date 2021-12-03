package com.discode.backend.persistence.query

class SearchMessageQuery(private val chatId: Long, queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    init {
        params["chatId"] = chatId
    }

    override fun getSql(): String {
        TODO("Not yet implemented")
    }
}