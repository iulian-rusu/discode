package com.discode.backend.api.controllers

import com.discode.backend.api.requests.CreateChatRequest
import com.discode.backend.api.requests.PostChatMemberRequest
import com.discode.backend.api.requests.PostMessageRequest
import com.discode.backend.api.requests.UpdateChatMemberRequest
import com.discode.backend.api.utils.HttpResponse
import com.discode.backend.api.utils.ScopeGuarded
import com.discode.backend.business.interfaces.ChatServiceInterface
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.ChatMember
import com.discode.backend.business.models.Message
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chats")
class ChatController : ScopeGuarded(ChatController::class) {
    @Autowired
    private lateinit var chatService: ChatServiceInterface

    @PostMapping("")
    fun postChat(
        @RequestBody request: CreateChatRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Chat> {
        return guardedWith(HttpStatus.NOT_ACCEPTABLE, "Invalid chat data") {
            HttpResponse.created(chatService.createChat(request, authHeader))
        }
    }

    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Chat> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat not found") {
            ResponseEntity.ok(chatService.deleteChat(chatId, authHeader))
        }
    }

    @GetMapping("/{chatId}/members")
    fun getAllMembers(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<ChatMember>> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat not found") {
            ResponseEntity.ok(chatService.getAllMembers(chatId, authHeader))
        }
    }

    @PostMapping("/{chatId}/members")
    fun postMember(
        @PathVariable chatId: Long,
        @RequestBody request: PostChatMemberRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<ChatMember> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat not found") {
            ResponseEntity.ok(chatService.addMember(chatId, request, authHeader))
        }
    }

    @PatchMapping("/{chatId}/members/{userId}")
    fun patchMember(
        @PathVariable chatId: Long,
        @PathVariable userId: Long,
        @RequestBody request: UpdateChatMemberRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<ChatMember> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat member not found") {
            val query = UpdateChatMemberQuery(chatId, userId, request)
            ResponseEntity.ok(chatService.updateMember(query, authHeader))
        }
    }

    @DeleteMapping("/{chatId}/members/{userId}")
    fun deleteMember(
        @PathVariable chatId: Long,
        @PathVariable userId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<ChatMember> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat member not found") {
            ResponseEntity.ok(chatService.deleteMember(chatId, userId, authHeader))
        }
    }

    @GetMapping("/{chatId}/messages")
    fun getAllMessages(
        @PathVariable chatId: Long,
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<Message>> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat not found") {
            val query = SearchMessageQuery(chatId, searchParams)
            ResponseEntity.ok(chatService.getAllMessages(query, authHeader))
        }
    }

    @PostMapping("/{chatId}/messages")
    fun postMessage(
        @PathVariable chatId: Long,
        @RequestBody request: PostMessageRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Message> {
        return guardedWith(HttpStatus.NOT_FOUND, "Chat member not found") {
            HttpResponse.created(chatService.postMessage(chatId, request, authHeader))
        }
    }
}