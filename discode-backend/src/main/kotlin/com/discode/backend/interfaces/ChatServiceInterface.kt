package com.discode.backend.interfaces

import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostChatMemberRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import org.springframework.http.ResponseEntity

interface ChatServiceInterface {
    fun postChat(request: CreateChatRequest, authHeader: String?): ResponseEntity<Chat>
    fun deleteChat(chatId: Long, authHeader: String?): ResponseEntity<Chat>
    fun getAllMembers(chatId: Long, authHeader: String?): ResponseEntity<List<ChatMember>>
    fun postMember(chatId: Long, request: PostChatMemberRequest, authHeader: String?): ResponseEntity<ChatMember>
    fun patchMember(query: UpdateChatMemberQuery, authHeader: String?): ResponseEntity<ChatMember>
    fun getAllMessages(query: SearchMessageQuery, authHeader: String?): ResponseEntity<List<Message>>
    fun postMessage(chatId: Long, request: PostMessageRequest, authHeader: String?): ResponseEntity<Message>
}