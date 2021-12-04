package com.discode.backend.controllers

import com.discode.backend.interfaces.ChatInterface
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.models.requests.UpdateChatMemberRequest
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chats")
class ChatController {
    @Autowired
    private lateinit var chatService: ChatInterface

    @GetMapping("")
    fun getAllChatsForUser(
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.getAllChatsForUser(searchParams, authHeader)

    @PostMapping("")
    fun postChat(@RequestBody(required = true) request: CreateChatRequest) = chatService.postChat(request)

    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.deleteChat(chatId, authHeader)

    @GetMapping("/{chatId}/members")
    fun getAllMembers(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.getAllMembers(chatId, authHeader)

    @PostMapping("/{chatId}/members")
    fun postMember(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.postMember(chatId, authHeader)

    @PatchMapping("/{chatId}/members/{userId}")
    fun patchMember(
        @PathVariable chatId: Long,
        @PathVariable userId: Long,
        @RequestBody(required = true) request: UpdateChatMemberRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.patchMember(UpdateChatMemberQuery(chatId, userId, request), authHeader)

    @GetMapping("/{chatId}/messages")
    fun getAllMessages(
        @PathVariable chatId: Long,
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.getAllMessages(SearchMessageQuery(chatId, searchParams), authHeader)

    @PostMapping("/{chatId}/messages")
    fun postMessage(
        @PathVariable chatId: Long,
        @RequestBody(required = true) request: PostMessageRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.postMessage(chatId, request, authHeader)
}