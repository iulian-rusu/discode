package com.discode.backend.persistence

import com.discode.backend.api.requests.CreateChatRequest
import com.discode.backend.api.requests.PostChatMemberRequest
import com.discode.backend.api.requests.PostMessageRequest
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.Message
import com.discode.backend.persistence.mappers.ChatMemberRowMapper
import com.discode.backend.persistence.mappers.ChatRowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement
import java.util.*


@Repository
class ChatRepository : RepositoryBase() {
    fun save(request: CreateChatRequest): Chat {
        val params = mapOf(
            "ownerId" to request.ownerId,
            "chatName" to request.chatName
        )
        namedJdbcTemplate.update(
            """
                START TRANSACTION;
                INSERT INTO chats (chat_name) VALUES (:chatName);
                INSERT INTO chat_members (chat_id, user_id, status) VALUES (
                    (SELECT chat_id FROM chats WHERE chat_name = :chatName),
                    :ownerId, 'o'
                );
                COMMIT;
            """, params
        )
        return findOne(request.chatName)
    }

    fun findOne(chatId: Long): Chat {
        return jdbcTemplate.query("SELECT * FROM chats WHERE chat_id = ?", ChatRowMapper(), chatId).first()
    }

    fun findOne(chatName: String): Chat {
        return jdbcTemplate.query("SELECT * FROM chats WHERE chat_name = ?", ChatRowMapper(), chatName).first()
    }

    fun deleteOne(chatId: Long) {
        jdbcTemplate.update("DELETE FROM chats WHERE chat_id = ?", chatId)
    }

    fun findOwnerId(chatId: Long): Long {
        return jdbcTemplate.queryForObject(
            "SELECT user_id FROM chat_members WHERE chat_id = ? AND status = 'o'",
            Long::class.java, chatId
        )
    }

    fun findAllMembers(chatId: Long): List<ChatMember> {
        return jdbcTemplate.query(
            "SELECT * FROM chat_members WHERE chat_id = ?", ChatMemberRowMapper(), chatId
        ).toList()
    }

    fun findMember(chatId: Long, userId: Long): ChatMember {
        return jdbcTemplate.query(
            "SELECT * FROM chat_members WHERE chat_id = ? LIMIT 1", ChatMemberRowMapper(), chatId
        ).first()
    }

    fun addMember(chatId: Long, reuqest: PostChatMemberRequest): ChatMember {
        jdbcTemplate.update(
            "INSERT INTO chat_members (chat_id, user_id, status) VALUES (?, ?, 'g')",
            chatId, reuqest.userId
        )
        return ChatMember(chatId, reuqest.userId, 'g')
    }

    fun isMember(chatId: Long, userId: Long): Boolean {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM chat_members WHERE chat_id = ? AND user_id = ?)",
            Boolean::class.java, chatId, userId
        )
    }

    fun addMessage(chatId: Long, request: PostMessageRequest): Message {
        val author = findMember(chatId, request.userId)
        val keyHolder = GeneratedKeyHolder()
        val sql = """
            INSERT INTO messages (chat_member_id, creation_date, content, code_output) 
            VALUES (
                (SELECT chat_member_id FROM chat_members WHERE chat_id = ? AND user_id = ?),
                SYSDATE(), ?, NULL
            )
        """
        jdbcTemplate.update({ connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                .apply {
                    setLong(1, chatId)
                    setLong(2, request.userId)
                    setString(3, request.content)
                }
        }, keyHolder)
        return Message(
            messageId = keyHolder.key?.toLong() ?: -1,
            author = author,
            creationDate = Date(),
            content = request.content,
            codeOutput = null
        )
    }
}