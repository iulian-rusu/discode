package com.discode.backend.persistence

import com.discode.backend.models.User
import com.discode.backend.models.UserCredentials
import com.discode.backend.models.UserProfile
import com.discode.backend.persistence.mappers.UserRowMapper
import com.discode.backend.security.Encoder
import org.springframework.stereotype.Repository

@Repository
class UserRepository : RepositoryBase() {
    fun save(credentials: UserCredentials, profile: UserProfile): Long {
        val passwordHash = Encoder.hashString(credentials.password)
        val params = mapOf(
            "username" to credentials.username,
            "passwordHash" to passwordHash,
            "firstName" to profile.firstName,
            "lastName" to profile.lastName,
            "email" to profile.email,
            "description" to profile.description,
            "imagePath" to profile.imagePath
        )
        namedJdbcTemplate.update(
            """
                START TRANSACTION;
                INSERT INTO user_credentials (username, password_hash) VALUES (:username, BINARY :passwordHash);
                INSERT INTO user_accounts (user_id, first_name, last_name, email, description, image_path) 
                VALUES (
                    (SELECT user_id FROM user_credentials WHERE username = :username),
                    :firstName, :lastName, :email, :description, :imagePath
                );
                COMMIT;
            """,
            params
        )

        return findId(credentials.username)
    }

    fun findId(username: String) =
        jdbcTemplate.queryForObject(
            "SELECT user_id FROM user_credentials WHERE username = ?", Long::class.java, username
        )

    fun findOne(userId: Long): User =
        jdbcTemplate.query(
            "SELECT * FROM user_credentials INNER JOIN user_accounts USING(user_id) WHERE user_id = ?",
            UserRowMapper(), userId
        ).first()

    fun deleteOne(userId: Long) {
        jdbcTemplate.update("DELETE FROM user_credentials WHERE user_id = ?", userId)
    }
}