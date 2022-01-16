package com.discode.backend.persistence.query

class SearchUserBanQuery(queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    private val userId: Long

    init {
        userId = queryParams["user_id"]?.toLongOrNull() ?: -1
        params["userId"] = userId
    }

    override fun getSql(): String {
        val sqlBuilder = StringBuilder("SELECT * FROM user_bans INNER JOIN user_credentials USING(user_id) ")
        if (userId >= 0) {
            sqlBuilder.append(" WHERE user_id = :userId ")
        }
        sqlBuilder.append("LIMIT :itemsPerPage OFFSET :offset")
        return sqlBuilder.toString()
    }
}