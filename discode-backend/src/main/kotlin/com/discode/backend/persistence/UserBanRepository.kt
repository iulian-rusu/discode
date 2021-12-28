package com.discode.backend.persistence

import com.discode.backend.api.requests.PostUserBanRequest
import com.discode.backend.business.models.UserBan
import com.discode.backend.persistence.mappers.UserBanRowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class UserBanRepository : RepositoryBase() {
    fun save(request: PostUserBanRequest): UserBan {
        val keyHolder = GeneratedKeyHolder()
        val sql = """ 
            INSERT INTO user_bans(user_id, start_date, end_date, ban_reason)
            VALUES (?, SYSDATE(), DATE_ADD(SYSDATE(), INTERVAL ? SECOND), ?)
        """.trimIndent()

        jdbcTemplate.update({ connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                .apply {
                    setLong(1, request.userId)
                    setLong(2, request.seconds)
                    setString(3, request.banReason)
                }
        }, keyHolder)
        return findOne(keyHolder.key?.toLong() ?: -1)
    }

    fun findOne(banId: Long): UserBan {
        return jdbcTemplate.query("SELECT * FROM user_bans WHERE ban_id = ?", UserBanRowMapper(), banId).first()
    }

    fun deleteOne(banId: Long): UserBan {
        val toDelete = findOne(banId)
        jdbcTemplate.update("DELETE FROM user_bans WHERE ban_id = ?", banId)
        return toDelete
    }

    fun findBansForUser(userId: Long): List<UserBan> {
        return jdbcTemplate.query(
            "SELECT * FROM user_bans WHERE user_id = ? ORDER BY end_date DESC",
            UserBanRowMapper(),
            userId
        )
    }

    fun isBanned(userId: Long): Boolean {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM user_bans WHERE user_id = ? AND end_date >= SYSDATE())",
            Long::class.java,
            userId
        ) == 1L
    }
}