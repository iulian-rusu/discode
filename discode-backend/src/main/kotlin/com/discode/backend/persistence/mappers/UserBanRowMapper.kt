package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.UserBan
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class UserBanRowMapper : RowMapper<UserBan> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        UserBan(
            banId = rs.getLong("ban_id"),
            userId = rs.getLong("user_id"),
            startDate = rs.getTimestamp("start_date"),
            endDate = rs.getTimestamp("end_date"),
            banReason = rs.getString("ban_reason")
        )
}