package com.discode.backend.business.services

import com.discode.backend.api.requests.CreateChatRequest
import com.discode.backend.api.requests.PostChatMemberRequest
import com.discode.backend.api.requests.PostMessageRequest
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.ChatMemberStatus
import com.discode.backend.business.models.Message
import com.discode.backend.business.security.SimpleUserDetails
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.business.services.interfaces.ChatServiceInterface
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
        return ifAuthorized(authHeader,
            authorizer = { details ->
                details.isAdmin || chatRepository.isOwner(chatId, details.userId)
            },
            action = {
                if (chatRepository.isMember(chatId, request.userId))
                    throw ResponseStatusException(HttpStatus.CONFLICT, "User is already in that chat")
                chatRepository.addMember(chatId, request)
            }
        )
    }

    override fun updateMember(query: UpdateChatMemberQuery, authHeader: String?): ChatMember {
        return ifAuthorized(
            header = authHeader,
            authorizer = { details ->
                authorizeMemberUpdate(details, query)
            },
            action = {
                genericQueryRepository.execute(query)
                chatRepository.findMember(query.chatId, query.userId)
            }
        )
    }

    override fun deleteMember(chatId: Long, userId: Long, authHeader: String?): ChatMember {
        return ifAdmin(authHeader) {
            chatRepository.deleteMember(chatId, userId)
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
                val chatMember = chatRepository.findMember(chatId, request.userId)
                if (chatMember.status == ChatMemberStatus.LEFT)
                    throw ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE,
                        "User has left the chat and cannot post messages"
                    )
                chatRepository.addMessage(chatMember, request)
            }
        )
    }

    private fun authorizeMemberUpdate(details: SimpleUserDetails, query: UpdateChatMemberQuery): Boolean {
        if (details.isAdmin)
            return true

        val isOwner = chatRepository.isOwner(query.chatId, details.userId)
        when (query.status) {
            ChatMemberStatus.OWNER -> throw ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot grant 'owner' status")
            ChatMemberStatus.GUEST -> {
                if (!isOwner)
                    throw ResponseStatusException(HttpStatus.FORBIDDEN, "Guests cannot invite other users")
                if (query.userId == details.userId)
                    throw ResponseStatusException(HttpStatus.CONFLICT, "Chat owner cannot self-assign as 'guest'")
                return true
            }
            ChatMemberStatus.LEFT -> {
                return if (!isOwner) {
                    if (query.userId != details.userId)
                        throw ResponseStatusException(HttpStatus.FORBIDDEN, "Guests cannot kick other users")
                    true
                } else {
                    if (query.userId == details.userId)
                        throw ResponseStatusException(HttpStatus.FORBIDDEN, "Owners cannot leave from their chats")
                    true
                }
            }
        }
    }
}