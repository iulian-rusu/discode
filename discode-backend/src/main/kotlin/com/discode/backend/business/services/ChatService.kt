package com.discode.backend.business.services

import com.discode.backend.api.requests.CreateChatRequest
import com.discode.backend.api.requests.PostChatMemberRequest
import com.discode.backend.api.requests.PostMessageRequest
import com.discode.backend.business.interfaces.ChatServiceInterface
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.Message
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.persistence.ChatRepository
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.mappers.MessageRowMapper
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ChatService : JwtAuthorized(), ChatServiceInterface {
    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var genericQueryRepository: GenericQueryRepository

    override fun createChat(request: CreateChatRequest, authHeader: String?): Chat {
        return ifAuthorizedAs(request.ownerId, authHeader) {
            chatRepository.save(request)
        }
    }

    override fun deleteChat(chatId: Long, authHeader: String?): Chat {
        val ownerId = chatRepository.findOwnerId(chatId)
        return ifAuthorizedAs(ownerId, authHeader) {
            val toDelete = chatRepository.findOne(chatId)
            chatRepository.deleteOne(toDelete.chatId)
            toDelete
        }
    }

    override fun getAllMembers(chatId: Long, authHeader: String?): List<ChatMember> {
        return ifAuthorized(
            header = authHeader,
            authorizer = { details ->
                try {
                    chatRepository.findOne(chatId)
                } catch (e: Exception) {
                    throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find chat")
                }
                chatRepository.isMember(chatId, details.userId) || details.isAdmin
            },
            action = {
                chatRepository.findAllMembers(chatId)
            }
        )
    }

    override fun addMember(chatId: Long, request: PostChatMemberRequest, authHeader: String?): ChatMember {
        return ifAuthorizedAs(request.userId, authHeader) {
            chatRepository.addMember(chatId, request)
        }
    }

    override fun updateMember(query: UpdateChatMemberQuery, authHeader: String?): ChatMember {
        return ifAuthorizedAs(query.userId, authHeader) {
            genericQueryRepository.execute(query)
            chatRepository.findMember(query.chatId, query.userId)
        }
    }

    override fun getAllMessages(query: SearchMessageQuery, authHeader: String?): List<Message> {
        return ifAuthorized(
            header = authHeader,
            authorizer = { details ->
                chatRepository.isMember(query.chatId, details.userId)
            },
            action = {
                genericQueryRepository.find(query, MessageRowMapper())
            }
        )
    }

    override fun postMessage(chatId: Long, request: PostMessageRequest, authHeader: String?): Message {
        return ifAuthorized(
            header = authHeader,
            authorizer = { details ->
                chatRepository.isMember(chatId, request.userId) && details.userId == request.userId
            },
            action = {
                chatRepository.addMessage(chatId, request)
            }
        )
    }
}