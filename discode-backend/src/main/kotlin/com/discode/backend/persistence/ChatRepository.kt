package com.discode.backend.persistence

import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.persistence.mappers.ChatMemberRowMapper
import com.discode.backend.persistence.mappers.ChatRowMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ChatRepository : RepositoryBase() {
    fun save(request: CreateChatRequest, ownerId: Long): Chat {
        val params = mapOf(
            "chatName" to request.chatName,
            "ownerId" to ownerId
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

    fun addMember(chatId: Long, userId: Long): ChatMember {
        jdbcTemplate.update(
            "INSERT INTO chat_members (chat_id, user_id, status) VALUES (?, ?, 'g')",
            chatId, userId
        )
        return ChatMember(chatId, userId, 'g')
    }

    fun isMember(chatId: Long, userId: Long): Boolean {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM chat_members WHERE chat_id = ? AND user_id = ?)",
            Boolean::class.java, chatId, userId
        )
    }

    fun addMessage(chatId: Long, userId: Long, request: PostMessageRequest): Message {
        val author = findMember(chatId, userId)
        jdbcTemplate.update(
            """
            INSERT INTO messages (chat_member_id, creation_date, content, code_output) 
            VALUES (
                (SELECT chat_member_id FROM chat_members WHERE chat_id = ? AND user_id = ?),
                SYSDATE(), ?, NULL
            )
        """, chatId, userId, request.content
        )
        return Message(
            author = author,
            creationData = Date(),
            content = request.content,
            codeOutput = null
        )
    }
}