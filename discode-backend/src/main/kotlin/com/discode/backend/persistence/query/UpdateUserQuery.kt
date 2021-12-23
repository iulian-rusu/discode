package com.discode.backend.persistence.query

class UpdateUserQuery(
    val userId: Long,
    val passwordHash: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val description: String?,
    val imagePath: String?
) : ParametrizedQuery() {

    init {
        params["userId"] = userId
        params["passwordHash"] = passwordHash
        params["firstName"] = firstName
        params["lastName"] = lastName
        params["email"] = email
        params["description"] = description
        params["imagePath"] = imagePath
    }

    override fun getSql(): String {
        val sqlBuilder = StringBuilder("START TRANSACTION; ")
        if (passwordHash != null)
            sqlBuilder.append("UPDATE user_credentials SET password_hash = :passwordHash WHERE user_id = :userId; ")

        val updateConditions = mutableListOf<String>()
        if (firstName != null)
            updateConditions.add("first_name = :firstName")
        if (lastName != null)
            updateConditions.add("last_name = :lastName")
        if (email != null)
            updateConditions.add("email = :email")
        description?.run {
            if (this.isNotEmpty())
                updateConditions.add("description = :description")
            else
                updateConditions.add("description = NULL")
        }
        imagePath?.run {
            if (this.isNotEmpty())
                updateConditions.add("image_path = :imagePath")
            else
                updateConditions.add("image_path = NULL")
        }
        if (updateConditions.isNotEmpty()) {
            sqlBuilder.append("UPDATE user_accounts SET ")
            sqlBuilder.append(updateConditions.joinToString(", "))
            sqlBuilder.append(" WHERE user_id = :userId; ")
        }
        sqlBuilder.append("COMMIT;")
        return sqlBuilder.toString()
    }
}