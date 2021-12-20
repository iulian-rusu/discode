package com.discode.backend.controllers

import com.discode.backend.controllers.util.HttpResponse
import com.discode.backend.interfaces.ChatServiceInterface
import com.discode.backend.models.Chat
import com.discode.backend.models.ChatMember
import com.discode.backend.models.Message
import com.discode.backend.models.requests.CreateChatRequest
import com.discode.backend.models.requests.PostChatMemberRequest
import com.discode.backend.models.requests.PostMessageRequest
import com.discode.backend.models.requests.UpdateChatMemberRequest
import com.discode.backend.persistence.query.SearchMessageQuery
import com.discode.backend.persistence.query.UpdateChatMemberQuery
import com.discode.backend.security.jwt.JwtAuthorized
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/chats")
class ChatController: JwtAuthorized() {
    @Autowired
    private lateinit var chatService: ChatServiceInterface

    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    @PostMapping("")
    fun postChat(
        @RequestBody(required = true) request: CreateChatRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Chat> {
        return try {
            authorizedScope {
                HttpResponse.created(chatService.postChat(request, authHeader))
            }
        } catch (e: Exception) {
            logger.error("POST /api/chats [ownerId=${request.ownerId}]: $e")
            throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid chat data")
        }
    }

    @DeleteMapping("/{chatId}")
    fun deleteChat(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Chat> {
        return try {
            authorizedScope {
                ResponseEntity.ok(chatService.deleteChat(chatId, authHeader))
            }
        } catch (e: Exception) {
            logger.error("DELETE /api/chats/$chatId: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found")
        }
    }

    @GetMapping("/{chatId}/members")
    fun getAllMembers(
        @PathVariable chatId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ) : ResponseEntity<List<ChatMember>> {
        return try {
           authorizedScope {
               ResponseEntity.ok(chatService.getAllMembers(chatId, authHeader))
           }
        } catch (e: Exception) {
            logger.error("GET /api/chats/$chatId/members: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found")
        }
    }

    @PostMapping("/{chatId}/members")
    fun postMember(
        @PathVariable chatId: Long,
        @RequestBody request: PostChatMemberRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<ChatMember> {
        return try {
            authorizedScope {
                ResponseEntity.ok(chatService.postMember(chatId, request, authHeader))
            }
        } catch (e: Exception) {
            logger.error("POST /api/chats/$chatId/members: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found")
        }
    }

    @PatchMapping("/{chatId}/members/{userId}")
    fun patchMember(
        @PathVariable chatId: Long,
        @PathVariable userId: Long,
        @RequestBody(required = true) request: UpdateChatMemberRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) : ResponseEntity<ChatMember> {
        return try {
            authorizedScope {
                val query = UpdateChatMemberQuery(chatId, userId, request)
                ResponseEntity.ok(chatService.patchMember(query, authHeader))
            }
        } catch (e: Exception) {
            logger.error("POST /api/chats/$chatId/members/$userId: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chat member not found")
        }
    }

    @GetMapping("/{chatId}/messages")
    fun getAllMessages(
        @PathVariable chatId: Long,
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ) : ResponseEntity<List<Message>>  {
        return try {
            authorizedScope {
                val query = SearchMessageQuery(chatId, searchParams)
                ResponseEntity.ok(chatService.getAllMessages(query, authHeader))
            }
        } catch (e: Exception) {
            logger.error("GET /api/chats/$chatId/messages: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found")
        }
    }

    @PostMapping("/{chatId}/messages")
    fun postMessage(
        @PathVariable chatId: Long,
        @RequestBody(required = true) request: PostMessageRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) : ResponseEntity<Message> {
        return try {
            authorizedScope {
                HttpResponse.created(chatService.postMessage(chatId, request, authHeader))
            }
        } catch (e: Exception) {
            logger.error("POST /api/chats/$chatId/messages: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found")
        }
    }
}