package com.discode.backend.interfaces

import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostChatMemberRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery

interface ChatServiceInterface {
    fun postChat(request: CreateChatRequest, authHeader: String?): Chat
    fun deleteChat(chatId: Long, authHeader: String?): Chat
    fun getAllMembers(chatId: Long, authHeader: String?): List<ChatMember>
    fun postMember(chatId: Long, request: PostChatMemberRequest, authHeader: String?): ChatMember
    fun patchMember(query: UpdateChatMemberQuery, authHeader: String?): ChatMember
    fun getAllMessages(query: SearchMessageQuery, authHeader: String?): List<Message>
    fun postMessage(chatId: Long, request: PostMessageRequest, authHeader: String?): Message
}