package com.discode.backend.persistence.query

class SearchReportQuery(queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    private val status: String?
    private val messageId: Long
    private val reporterId: Long

    init {
        status = queryParams["status"]
        messageId = queryParams["message_id"]?.toLongOrNull() ?: -1
        reporterId = queryParams["reporter_id"]?.toLongOrNull() ?: -1

        params["status"] = status
        params["messageId"] = messageId
        params["reporterId"] = reporterId
    }

    override fun getSql(): String {
        val sqlBuilder = StringBuilder("SELECT * FROM message_reports ")

        val conditions = mutableListOf<String>()
        if (status != null) {
            conditions.add("status = :status")
        }
        if (messageId >= 0) {
            conditions.add("message_id = :messageId")
        }
        if (reporterId >= 0) {
            conditions.add("reporter_id = :reporterId")
        }
        if (conditions.size > 0) {
            sqlBuilder.append(conditions.joinToString(separator = " AND ", prefix = "WHERE "))
        }

        sqlBuilder.append(" ORDER BY report_date DESC LIMIT :itemsPerPage OFFSET :offset")
        return sqlBuilder.toString()
    }
}