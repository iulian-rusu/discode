package com.discode.backend.persistence.query

abstract class PagedSearchCriteria(queryParams: Map<String, String>) : QueryCriteria {
    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 10
    }

    private val page: Int
    protected val itemsPerPage: Int
    protected val exactMatch: Boolean
    protected val offset: Int

    init {
        page = queryParams["page"]?.toIntOrNull() ?: DEFAULT_PAGE
        itemsPerPage = queryParams["items_per_page"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE
        exactMatch = queryParams["match"]?.let { it == "exact" } ?: false
        offset = (page - 1) * itemsPerPage
    }
}