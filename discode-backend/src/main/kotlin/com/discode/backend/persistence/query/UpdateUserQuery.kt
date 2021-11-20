package com.discode.backend.persistence.query

class UpdateUserQuery(
    val userId: Long,
    val username: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val description: String,
    val imagePath: String
) : ParametrizedQuery() {

    init {
        params["userId"] = userId
        params["username"] = username
        params["passwordHash"] = passwordHash
        params["firstName"] = firstName
        params["lastName"] = lastName
        params["email"] = email
        params["description"] = description
        params["imagePath"] = imagePath
    }

    override fun getSql(): String {
        val updateConditions = mutableListOf<String>()
        val sqlBuilder = StringBuilder("START TRANSACTION; ")
        if (username.isNotEmpty())
            updateConditions.add("username = :username")
        if (passwordHash.isNotEmpty())
            updateConditions.add("password_hash = :passwordHash")
        if (updateConditions.isNotEmpty()) {
            sqlBuilder.append("UPDATE user_credentials SET ")
            sqlBuilder.append(updateConditions.joinToString(", "))
            sqlBuilder.append(" WHERE user_id = :userId; ")
        }
        updateConditions.clear()
        if (firstName.isNotEmpty())
            updateConditions.add("first_name = :firstName")
        if (lastName.isNotEmpty())
            updateConditions.add("last_name = :lastName")
        if (email.isNotEmpty())
            updateConditions.add("email = :email")
        if (description.isNotEmpty())
            updateConditions.add("description = :description")
        if (imagePath.isNotEmpty())
            updateConditions.add("image_path = :imagePath")
        if (updateConditions.isNotEmpty()) {
            sqlBuilder.append("UPDATE user_accounts SET ")
            sqlBuilder.append(updateConditions.joinToString(", "))
            sqlBuilder.append(" WHERE user_id = :userId; ")
        }
        sqlBuilder.append("COMMIT;")
        return sqlBuilder.toString()
    }
}