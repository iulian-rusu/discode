package com.discode.backend.controllers

import com.discode.backend.interfaces.ChatInterface
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.models.requests.UpdateChatMemberRequest
import com.discode.backend.persistence.query.SearchMessageQuery
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

    @GetMapping("/{chatId}")
    fun getChat(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.getChat(chatId, authHeader)

    @PostMapping("")
    fun postChat(@RequestBody(required = true) request: CreateChatRequest) = chatService.postChat(request)

    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.deleteChat(chatId, authHeader)

    @GetMapping("/{chatId}/members")
    fun getMembers(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.getMembers(chatId, authHeader)

    @PostMapping("/{chatId}/members")
    fun postMember(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.postMember(chatId, authHeader)

    @PatchMapping("/members/{chatMemberId}")
    fun patchMember(
        @PathVariable chatMemberId: Long,
        @RequestBody(required = true) request: UpdateChatMemberRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.patchMember(chatMemberId, request, authHeader)

    @GetMapping("/{chatId}/messages")
    fun getAllMessages(
        @PathVariable chatId: Long,
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.getAllMessages(SearchMessageQuery(chatId, searchParams), authHeader)

    @PostMapping("/messages")
    fun postMessage(
        @RequestBody(required = true) request: PostMessageRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) = chatService.postMessage(request, authHeader)
}