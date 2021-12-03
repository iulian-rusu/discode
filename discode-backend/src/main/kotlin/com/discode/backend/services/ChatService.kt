package com.discode.backend.services

import com.discode.backend.interfaces.ChatInterface
import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.models.requests.UpdateChatMemberRequest
import com.discode.backend.persistence.query.SearchMessageQuery
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ChatService : ChatInterface {
    override fun getAllChatsForUser(
        searchParams: Map<String, String>,
        authHeader: String?
    ): ResponseEntity<List<Chat>> {
        TODO("Not yet implemented")
    }

    override fun getChat(chatId: Long, authHeader: String?): ResponseEntity<List<Chat>> {
        TODO("Not yet implemented")
    }

    override fun postChat(request: CreateChatRequest): ResponseEntity<Chat> {
        TODO("Not yet implemented")
    }

    override fun deleteChat(chatId: Long, authHeader: String?): ResponseEntity<Chat> {
        TODO("Not yet implemented")
    }

    override fun getMembers(chatId: Long, authHeader: String?): ResponseEntity<List<ChatMember>> {
        TODO("Not yet implemented")
    }

    override fun postMember(chatId: Long, authHeader: String?): ResponseEntity<ChatMember> {
        TODO("Not yet implemented")
    }

    override fun patchMember(
        chatMemberId: Long,
        request: UpdateChatMemberRequest,
        authHeader: String?
    ): ResponseEntity<ChatMember> {
        TODO("Not yet implemented")
    }

    override fun getAllMessages(query: SearchMessageQuery, authHeader: String?): ResponseEntity<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun postMessage(request: PostMessageRequest, authHeader: String?): ResponseEntity<Message> {
        TODO("Not yet implemented")
    }
}