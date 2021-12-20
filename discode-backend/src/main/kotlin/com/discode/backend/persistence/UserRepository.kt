package com.discode.backend.persistence

import com.discode.backend.models.User
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.persistence.mappers.UserRowMapper
import com.discode.backend.security.Encoder
import org.springframework.stereotype.Repository

@Repository
class UserRepository : RepositoryBase() {
    fun save(request: RegisterUserRequest): User {
        val passwordHash = Encoder.hashString(request.password)
        val params = mapOf(
            "username" to request.username,
            "passwordHash" to passwordHash,
            "firstName" to request.firstName,
            "lastName" to request.lastName,
            "email" to request.email
        )

        namedJdbcTemplate.update(
            "INSERT INTO user_credentials (username, password_hash) VALUES (:username, BINARY :passwordHash)",
            params
        )
        try {
            namedJdbcTemplate.update(
                """
                INSERT INTO user_accounts (user_id, first_name, last_name, email) 
                VALUES (
                    (SELECT user_id FROM user_credentials WHERE username = :username),
                    :firstName, :lastName, :email
                )
            """,
                params
            )
        } catch (e: Exception) {
            jdbcTemplate.update("DELETE FROM user_credentials WHERE username = ?", request.username)
            throw e
        }

        return findOne(request.username)
    }

    fun findOne(username: String): User =
        jdbcTemplate.query(
            "SELECT * FROM user_credentials INNER JOIN user_accounts USING(user_id) WHERE username = ?",
            UserRowMapper(), username
        ).first()

    fun findOne(userId: Long): User =
        jdbcTemplate.query(
            "SELECT * FROM user_credentials INNER JOIN user_accounts USING(user_id) WHERE user_id = ?",
            UserRowMapper(), userId
        ).first()

    fun deleteOne(userId: Long) {
        jdbcTemplate.update("DELETE FROM user_credentials WHERE user_id = ?", userId)
    }

    fun findPasswordHash(username: String) =
        jdbcTemplate.queryForObject(
            "SELECT password_hash FROM user_credentials WHERE username = ?",
            String::class.java,
            username
        )

    fun isAdmin(userId: Long) =
        jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM admins WHERE user_id = ?)",
            Long::class.java,
            userId
        ) == 1L
}