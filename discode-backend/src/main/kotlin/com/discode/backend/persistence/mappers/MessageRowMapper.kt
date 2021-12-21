package com.discode.backend.persistence.mappers

import com.discode.backend.business.models.Message
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class MessageRowMapper : RowMapper<Message> {
    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Message(
            messageId = rs.getLong("message_id"),
            author = ChatMemberRowMapper().mapRow(rs, rowNum),
            creationDate = rs.getDate("creation_date"),
            content = rs.getString("content"),
            codeOutput = rs.getString("code_output")
        )
}