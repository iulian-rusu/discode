package com.discode.backend.persistence

import com.discode.backend.api.requests.CreateChatRequest
import com.discode.backend.api.requests.PostChatMemberRequest
import com.discode.backend.api.requests.PostMessageRequest
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.ChatMemberStatus
import com.discode.backend.business.models.Message
import com.discode.backend.persistence.mappers.ChatMemberRowMapper
import com.discode.backend.persistence.mappers.ChatRowMapper
import com.discode.backend.persistence.mappers.MessageRowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement
import java.util.*

@Repository
class ChatRepository : RepositoryBase() {
    fun save(request: CreateChatRequest): Chat {
        val params = mapOf(
            "ownerId" to request.ownerId,
            "chatName" to request.chatName,
            "ownerStatus" to ChatMemberStatus.OWNER.toString()
        )
        namedJdbcTemplate.update(
            """
                START TRANSACTION;
                INSERT INTO chats (chat_name) VALUES (:chatName);
                INSERT INTO chat_members (chat_id, user_id, status) VALUES (
                    LAST_INSERT_ID(), :ownerId, :ownerStatus
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
            "SELECT user_id FROM chat_members WHERE chat_id = ? AND status = ?",
            Long::class.java, chatId, ChatMemberStatus.OWNER.toString()
        )
    }

    fun findAllMembers(chatId: Long): List<ChatMember> {
        return jdbcTemplate.query(
            """
            SELECT * FROM chat_members
            INNER JOIN user_credentials USING(user_id)
            INNER JOIN user_accounts USING(user_id)
            WHERE chat_id = ?
            """, ChatMemberRowMapper(), chatId
        ).toList()
    }

    fun findMember(chatId: Long, userId: Long): ChatMember {
        return jdbcTemplate.query(
            """
            SELECT * FROM chat_members
            INNER JOIN user_credentials USING(user_id)
            INNER JOIN user_accounts USING(user_id)
            WHERE chat_id = ? AND user_id = ? LIMIT 1
            """,
            ChatMemberRowMapper(), chatId, userId
        ).first()
    }

    fun addMember(chatId: Long, request: PostChatMemberRequest): ChatMember {
        val keyHolder = GeneratedKeyHolder()
        val sql = "INSERT INTO chat_members (chat_id, user_id, status) VALUES (?, ?, ?)"
        jdbcTemplate.update({ connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                .apply {
                    setLong(1, chatId)
                    setLong(2, request.userId)
                    setString(3, ChatMemberStatus.GUEST.toString())
                }
        }, keyHolder)
        return findMember(chatId, request.userId)
    }

    fun deleteMember(chatId: Long, userId: Long): ChatMember {
        val member = findMember(chatId, userId)
        jdbcTemplate.update(
            "DELETE FROM chat_members WHERE chat_id = ? AND user_id = ?",
            chatId, userId
        )
        return member
    }

    fun isMember(chatId: Long, userId: Long): Boolean {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM chat_members WHERE chat_id = ? AND user_id = ?)",
            Boolean::class.java, chatId, userId
        )
    }

    fun isOwner(chatId: Long, userId: Long): Boolean {
        return jdbcTemplate.queryForObject(
            "SELECT EXISTS (SELECT * FROM chat_members WHERE chat_id = ? AND user_id = ? AND status = ?)",
            Boolean::class.java, chatId, userId, ChatMemberStatus.OWNER.toString()
        )
    }

    fun addMessage(author: ChatMember, request: PostMessageRequest): Message {
        val keyHolder = GeneratedKeyHolder()
        val sql = """
            INSERT INTO messages (chat_member_id, creation_date, content, code_output) 
            VALUES (?, SYSDATE(), ?, NULL)
        """
        jdbcTemplate.update({ connection ->
            connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                .apply {
                    setLong(1, author.chatMemberId)
                    setString(2, request.content)
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

    fun findMessage(messageId: Long): Message {
        return jdbcTemplate.query(
            "SELECT * FROM messages m INNER JOIN chat_members cm USING (chat_member_id) WHERE message_id = ?",
            MessageRowMapper(), messageId
        ).first()
    }

    fun updateCodeOutput(message: Message): Message {
        return message.also {
            jdbcTemplate.update(
                "UPDATE messages SET code_output = ? WHERE message_id = ?",
                it.codeOutput,
                it.messageId
            )
        }
    }
}