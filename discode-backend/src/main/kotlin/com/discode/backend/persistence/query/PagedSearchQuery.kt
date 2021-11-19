package com.discode.backend.persistence.query


abstract class PagedSearchQuery(queryParams: Map<String, String>) : ParametrizedQuery() {
    companion object {
        const val DEFAULT_PAGE = 1
        const val DEFAULT_PAGE_SIZE = 10
    }

    private val page: Int
    private val itemsPerPage: Int
    private val offset: Int
    protected val exactMatch: Boolean

    init {
        page = queryParams["page"]?.toIntOrNull() ?: DEFAULT_PAGE
        itemsPerPage = queryParams["items_per_page"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE
        exactMatch = queryParams["match"]?.let { it == "exact" } ?: false
        offset = (page - 1) * itemsPerPage

        params["itemsPerPage"] = itemsPerPage
        params["offset"] = offset
    }
}