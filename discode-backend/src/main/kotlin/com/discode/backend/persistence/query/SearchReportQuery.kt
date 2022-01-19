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
        val sqlBuilder = StringBuilder("""
            SELECT 
                mr.*, 
                reporter_creds.user_id AS reporter_id, 
                reporter_creds.username AS reporter_username,
                reported_creds.message AS message,
                reported_id, 
                reported_username
            FROM message_reports mr
            INNER JOIN user_credentials reporter_creds ON mr.reporter_id = reporter_creds.user_id
            INNER JOIN (
                SELECT user_id AS reported_id, username AS reported_username, message_id, content AS message
                FROM user_credentials
                INNER JOIN chat_members USING(user_id)
                INNER JOIN messages USING(chat_member_id)
                INNER JOIN message_reports USING(message_id)
            ) reported_creds USING(message_id)
        """.trimMargin())

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
            sqlBuilder.append(conditions.joinToString(separator = " AND ", prefix = " WHERE "))
        }

        sqlBuilder.append(" ORDER BY report_date DESC LIMIT :itemsPerPage OFFSET :offset")
        return sqlBuilder.toString()
    }
}