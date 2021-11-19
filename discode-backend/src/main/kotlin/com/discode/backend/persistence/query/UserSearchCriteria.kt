package com.discode.backend.persistence.query

class UserSearchCriteria(queryParams: Map<String, String>) : PagedSearchCriteria(queryParams) {
    private val username: String
    private val firstName: String
    private val lastName: String
    private val paramMap: Map<String, Any>

    init {
        username = queryParams["username"] ?: ""
        firstName = queryParams["first_name"] ?: ""
        lastName = queryParams["last_name"] ?: ""
        paramMap = mapOf(
            "username" to username,
            "firstName" to firstName,
            "lastName" to lastName,
            "itemsPerPage" to itemsPerPage,
            "offset" to offset
        )
    }

    override fun getParams() = paramMap

    override fun getQuery(): String {
        val queryBuilder = StringBuilder("SELECT * FROM user_credentials INNER JOIN user_accounts USING (user_id) ")
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
                        " LOCATE(LOWER(:firstName), LOWER(last_name)) AND" +
                        " LOCATE(LOWER(:lastName), LOWER(last_name)) AND"
            )
        }
        conditionBuilder.append(" 1 LIMIT :itemsPerPage OFFSET :offset")
        if (conditionBuilder.length > initialLength)
            queryBuilder.append(conditionBuilder)
        return queryBuilder.toString()
    }
}