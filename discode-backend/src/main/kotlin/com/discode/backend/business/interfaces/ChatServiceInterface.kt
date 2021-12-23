package com.discode.backend.business.interfaces

import com.discode.backend.api.requests.CreateChatRequest
import com.discode.backend.api.requests.PostChatMemberRequest
import com.discode.backend.api.requests.PostMessageRequest
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.Message
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery

interface ChatServiceInterface {
    fun createChat(request: CreateChatRequest, authHeader: String?): Chat
    fun deleteChat(chatId: Long, authHeader: String?): Chat
    fun getAllMembers(chatId: Long, authHeader: String?): List<ChatMember>
    fun addMember(chatId: Long, request: PostChatMemberRequest, authHeader: String?): ChatMember
    fun updateMember(query: UpdateChatMemberQuery, authHeader: String?): ChatMember
    fun deleteMember(chatId: Long, userId: Long, authHeader: String?): ChatMember
    fun getAllMessages(query: SearchMessageQuery, authHeader: String?): List<Message>
    fun postMessage(chatId: Long, request: PostMessageRequest, authHeader: String?): Message
}