package com.discode.backend.interfaces

import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.models.requests.UpdateChatMemberRequest
import com.discode.backend.persistence.query.SearchMessageQuery
import org.springframework.http.ResponseEntity

interface ChatInterface {
    fun getAllChatsForUser(searchParams: Map<String, String>, authHeader: String?): ResponseEntity<List<Chat>>
    fun postChat(request: CreateChatRequest): ResponseEntity<Chat>
    fun deleteChat(chatId: Long, authHeader: String?): ResponseEntity<Chat>
    fun getAllMembers(chatId: Long, authHeader: String?): ResponseEntity<List<ChatMember>>
    fun postMember(chatId: Long, authHeader: String?): ResponseEntity<ChatMember>
    fun patchMember(chatMemberId: Long, request: UpdateChatMemberRequest, authHeader: String?): ResponseEntity<ChatMember>
    fun getAllMessages(query: SearchMessageQuery, authHeader: String?): ResponseEntity<List<Message>>
    fun postMessage(request: PostMessageRequest, authHeader: String?): ResponseEntity<Message>
}