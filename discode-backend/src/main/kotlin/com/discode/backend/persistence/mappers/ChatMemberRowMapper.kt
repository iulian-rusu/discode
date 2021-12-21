package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.ChatMember
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ChatMemberRowMapper : RowMapper<ChatMember> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        ChatMember(
            chatId = rs.getLong("chat_id"),
            userId = rs.getLong("user_id"),
            status = rs.getString("status").first()
        )
}