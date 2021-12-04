package com.discode.backend.services

import com.discode.backend.interfaces.ChatInterface
import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.mappers.ChatRowMapper
import com.discode.backend.persistence.query.SearchChatQuery
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import com.discode.backend.security.jwt.JwtAuthorizedService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ChatService : JwtAuthorizedService(), ChatInterface {
    @Autowired
    private lateinit var genericQueryRepository: GenericQueryRepository

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    override fun getAllChatsForUser(
        searchParams: Map<String, String>,
        authHeader: String?
    ): ResponseEntity<List<Chat>> {
        return try {
            val token = jwtProvider.getToken(authHeader)!!
            val userId = jwtProvider.getUserId(token)
            val query = SearchChatQuery(userId, searchParams)
            genericQueryRepository.find(query, ChatRowMapper())
                .toList()
                .let { ResponseEntity.ok(it) }
        } catch (e: Exception) {
            logger.error("getAllChatsForUser(): $e")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    override fun postChat(request: CreateChatRequest): ResponseEntity<Chat> {
        TODO("Not yet implemented")
    }

    override fun deleteChat(chatId: Long, authHeader: String?): ResponseEntity<Chat> {
        TODO("Not yet implemented")
    }

    override fun getAllMembers(chatId: Long, authHeader: String?): ResponseEntity<List<ChatMember>> {
        TODO("Not yet implemented")
    }

    override fun postMember(chatId: Long, authHeader: String?): ResponseEntity<ChatMember> {
        TODO("Not yet implemented")
    }

    override fun patchMember(query: UpdateChatMemberQuery, authHeader: String?): ResponseEntity<ChatMember> {
        TODO("Not yet implemented")
    }

    override fun getAllMessages(query: SearchMessageQuery, authHeader: String?): ResponseEntity<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun postMessage(chatId: Long, request: PostMessageRequest, authHeader: String?): ResponseEntity<Message> {
        TODO("Not yet implemented")
    }
}