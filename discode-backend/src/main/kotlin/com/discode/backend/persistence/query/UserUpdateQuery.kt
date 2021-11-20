package com.discode.backend.persistence.query

class UserUpdateQuery(val userId: Long, patchedValues: Map<String, String>) : ParametrizedQuery() {
    private val username: String
    private val passwordHash: String
    private val firstName: String
    private val lastName: String
    private val email: String
    private val description: String
    private val imagePath: String

    init {
        username = patchedValues["username"] ?: ""
        passwordHash = patchedValues["passwordHash"] ?: ""
        firstName = patchedValues["firstName"] ?: ""
        lastName = patchedValues["lastName"] ?: ""
        email = patchedValues["email"] ?: ""
        description = patchedValues["description"] ?: ""
        imagePath = patchedValues["imagePath"] ?: ""

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
        val sqlBuilder = StringBuilder("START TRANSACTION; ")
        val updateConditions = mutableListOf<String>()

        sqlBuilder.append("UPDATE user_credentials SET ")
        if (username.isNotEmpty())
            updateConditions.add("username = :username")
        else
            updateConditions.add("username = username")
        if (passwordHash.isNotEmpty())
            updateConditions.add("password_hash = :passwordHash")
        sqlBuilder.append(updateConditions.joinToString(", "))
        sqlBuilder.append(" WHERE user_id = :userId; ")

        updateConditions.clear()

        sqlBuilder.append("UPDATE user_accounts SET ")
        if (firstName.isNotEmpty())
            updateConditions.add("first_name = :firstName")
        else
            updateConditions.add("first_name = first_name")
        if (lastName.isNotEmpty())
            updateConditions.add("last_name = :lastName")
        if (email.isNotEmpty())
            updateConditions.add("email = :email")
        if (description.isNotEmpty())
            updateConditions.add("description = :description")
        if (imagePath.isNotEmpty())
            updateConditions.add("image_path = :imagePath")
        sqlBuilder.append(updateConditions.joinToString(", "))
        sqlBuilder.append(" WHERE user_id = :userId; ")
        sqlBuilder.append("COMMIT;")

        return sqlBuilder.toString()
    }
}