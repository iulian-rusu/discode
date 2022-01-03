package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.ChatMemberStatus
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ChatMemberRowMapper : RowMapper<ChatMember> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        ChatMember(
            chatMemberId = rs.getLong("chat_member_id"),
            chatId = rs.getLong("chat_id"),
            userId = rs.getLong("user_id"),
            username = rs.getString("username"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            imagePath = rs.getString("image_path"),
            status = ChatMemberStatus.valueOf(rs.getString("status"))
        )
}