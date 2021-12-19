package com.discode.backend.services

import com.discode.backend.interfaces.ChatServiceInterface
import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostChatMemberRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.persistence.ChatRepository
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.mappers.MessageRowMapper
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import com.discode.backend.security.jwt.JwtAuthorizedService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ChatService : JwtAuthorizedService(), ChatServiceInterface {
    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var genericQueryRepository: GenericQueryRepository

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    override fun postChat(request: CreateChatRequest, authHeader: String?): ResponseEntity<Chat> {
        return try {
            ifAuthorizedOn(request.ownerId, authHeader) {
                chatRepository.save(request).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
            }
        } catch (e: Exception) {
            logger.error("postChat(ownerId=${request.ownerId}): $e")
            ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
        }
    }

    override fun deleteChat(chatId: Long, authHeader: String?): ResponseEntity<Chat> {
        return try {
            val ownerId = chatRepository.findOwnerId(chatId)
            ifAuthorizedOn(ownerId, authHeader) {
                val toDelete = chatRepository.findOne(chatId)
                chatRepository.deleteOne(toDelete.chatId)
                ResponseEntity.ok(toDelete)
            }
        } catch (e: Exception) {
            logger.error("deleteChat(chatId=$chatId): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun getAllMembers(chatId: Long, authHeader: String?): ResponseEntity<List<ChatMember>> {
        return try {
            ifAuthorized(
                header = authHeader,
                authorizer = { details ->
                    chatRepository.isMember(chatId, details.userId) || details.isAdmin
                },
                action = {
                    ResponseEntity.ok(chatRepository.findAllMembers(chatId))
                }
            )
        } catch (e: Exception) {
            logger.error("getAllMembers(chatId=$chatId): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun postMember(
        chatId: Long,
        request: PostChatMemberRequest,
        authHeader: String?
    ): ResponseEntity<ChatMember> {
        return try {
            ifAuthorizedOn(request.userId, authHeader) {
                ResponseEntity.status(HttpStatus.CREATED).body(chatRepository.addMember(chatId, request))
            }
        } catch (e: Exception) {
            logger.error("postMember(chatId=$chatId): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun patchMember(query: UpdateChatMemberQuery, authHeader: String?): ResponseEntity<ChatMember> {
        return try {
            ifAuthorizedOn(query.userId, authHeader) {
                genericQueryRepository.execute(query)
                ResponseEntity.ok(chatRepository.findMember(query.chatId, query.userId))
            }
        } catch (e: Exception) {
            logger.error("patchMember(): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun getAllMessages(query: SearchMessageQuery, authHeader: String?): ResponseEntity<List<Message>> {
        return try {
            ifAuthorized(
                header = authHeader,
                authorizer = { details ->
                    chatRepository.isMember(query.chatId, details.userId)
                },
                action = {
                    ResponseEntity.ok(genericQueryRepository.find(query, MessageRowMapper()))
                }
            )
        } catch (e: Exception) {
            logger.error("getAllMessages(chatId=${query.chatId}): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    override fun postMessage(chatId: Long, request: PostMessageRequest, authHeader: String?): ResponseEntity<Message> {
        return try {
            ifAuthorized(
                header = authHeader,
                authorizer = { details ->
                    chatRepository.isMember(chatId, request.userId) && details.userId == request.userId
                },
                action = {
                    ResponseEntity.status(HttpStatus.CREATED).body(chatRepository.addMessage(chatId, request))
                }
            )
        } catch (e: Exception) {
            logger.error("postMessage(chatId=$chatId, userId=${request.userId}): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }
}