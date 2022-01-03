package com.discode.backend.persistence

import com.discode.backend.api.requests.RegisterUserRequest
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMemberStatus
import com.discode.backend.business.models.User
import com.discode.backend.business.security.Encoder
import com.discode.backend.persistence.mappers.ChatRowMapper
import com.discode.backend.persistence.mappers.UserRowMapper
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

    fun findOne(username: String): User {
        return jdbcTemplate.query(
            "SELECT * FROM user_credentials INNER JOIN user_accounts USING(user_id) WHERE username = ?",
            UserRowMapper(), username
        ).first()
    }

    fun findOne(userId: Long): User {
        return jdbcTemplate.query(
            "SELECT * FROM user_credentials INNER JOIN user_accounts USING(user_id) WHERE user_id = ?",
            UserRowMapper(), userId
        ).first()
    }

    fun findChats(userId: Long): List<Chat> {
        return jdbcTemplate.query(
            """
            SELECT * FROM chats c
            WHERE c.chat_id IN (SELECT chat_id FROM chat_members cm WHERE cm.user_id = ? AND cm.status <> ?)
           """, ChatRowMapper(), userId, ChatMemberStatus.LEFT.toString()
        )
    }

    fun deleteOne(userId: Long) {
        jdbcTemplate.update("DELETE FROM user_credentials WHERE user_id = ?", userId)
    }

    fun findPasswordHash(username: String): String {
        return jdbcTemplate.queryForObject(
            "SELECT password_hash FROM user_credentials WHERE username = ?",
            String::class.java,
            username
        )
    }

    fun isAdmin(userId: Long): Boolean {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM admins WHERE user_id = ?)",
            Long::class.java,
            userId
        ) == 1L
    }
}