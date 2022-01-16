package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.UserBan
import com.discode.backend.business.models.UserIdentification
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class UserBanRowMapper : RowMapper<UserBan> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        UserBan(
            banId = rs.getLong("ban_id"),
            user =  UserIdentification(
                userId = rs.getLong("user_id"),
                username = rs.getString("username")
            ),
            startDate = rs.getTimestamp("start_date"),
            endDate = rs.getTimestamp("end_date"),
            reason = rs.getString("ban_reason")
        )
}