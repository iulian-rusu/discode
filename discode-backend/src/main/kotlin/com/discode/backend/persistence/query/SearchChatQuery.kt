package com.discode.backend.persistence.query

class SearchChatQuery(private val userId: Long, queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    init {
        params["userId"] = userId
    }

    override fun getSql(): String {
        TODO("Not yet implemented")
    }
}