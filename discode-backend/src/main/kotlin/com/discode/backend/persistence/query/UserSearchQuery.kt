package com.discode.backend.persistence.query

class UserSearchQuery(queryParams: Map<String, String>) : PagedSearchQuery(queryParams) {
    private val username: String
    private val firstName: String
    private val lastName: String

    init {
        username = queryParams["username"] ?: ""
        firstName = queryParams["first_name"] ?: ""
        lastName = queryParams["last_name"] ?: ""

        params["username"] = username
        params["firstName"] = firstName
    }

    override fun getSql(): String {
        val sqlBuilder = StringBuilder("SELECT * FROM user_credentials INNER JOIN user_accounts USING (user_id) ")
        val conditionBuilder = StringBuilder(" WHERE")
        val initialLength = conditionBuilder.length
        if (exactMatch) {
            if (username.isNotEmpty())
                conditionBuilder.append(" username = :username AND")
            if (firstName.isNotEmpty())
                conditionBuilder.append(" first_name = :firstName AND")
            if (lastName.isNotEmpty())
                conditionBuilder.append(" last_name = :lastName AND")
        } else {
            conditionBuilder.append(
                " LOCATE(LOWER(:username), LOWER(username)) AND" +
                        " LOCATE(LOWER(:firstName), LOWER(first_name)) AND" +
                        " LOCATE(LOWER(:lastName), LOWER(last_name)) AND"
            )
        }
        conditionBuilder.append(" 1 LIMIT :itemsPerPage OFFSET :offset")
        if (conditionBuilder.length > initialLength)
            sqlBuilder.append(conditionBuilder)
        return sqlBuilder.toString()
    }
}