package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.Chat
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ChatRowMapper : RowMapper<Chat> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Chat(
            chatId = rs.getLong("chat_id"),
            chatName = rs.getString("chat_name")
        )
}