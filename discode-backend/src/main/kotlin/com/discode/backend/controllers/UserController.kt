package com.discode.backend.controllers

import com.discode.backend.controllers.util.HttpResponse
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.interfaces.UserServiceInterface
import com.discode.backend.models.Chat
import com.discode.backend.models.User
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.query.SearchUserQuery
import com.discode.backend.security.jwt.JwtAuthorized
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController: JwtAuthorized() {
    @Autowired
    private lateinit var userService: UserServiceInterface

    private val logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("")
    fun getAllUsers(@RequestParam searchParams: Map<String, String>): ResponseEntity<List<User>> {
        return try {
            authorizedScope {
                ResponseEntity.ok(userService.getAllUsers(SearchUserQuery(searchParams)))
            }
        } catch (e: Exception) {
            logger.error("GET /api/users: $e")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot process request")
        }
    }

    @PostMapping("")
    fun postUser(@RequestBody(required = true) request: RegisterUserRequest): ResponseEntity<AuthResponse> {
        return try {
            authorizedScope {
                HttpResponse.created(userService.postUser(request))
            }
        } catch (e: Exception) {
            logger.error("POST /api/users: $e")
            throw ResponseStatusException(HttpStatus.CONFLICT, "Conflicting request data")
        }
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long, @RequestHeader("Authorization") authHeader: String?): ResponseEntity<User> {
        return try {
            authorizedScope {
                ResponseEntity.ok(userService.getUser(userId, authHeader))
            }
        } catch (e: Exception) {
            logger.error("GET /api/users/$userId: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find user")
        }
    }

    @PatchMapping("/{userId}")
    fun patchUser(
        @PathVariable userId: Long,
        @RequestBody(required = true) request: UpdateUserRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<User> {
        return try {
            authorizedScope {
                ResponseEntity.ok(userService.patchUser(userId, request, authHeader))
            }
        } catch (e: Exception) {
            logger.error("PATCH /api/users/$userId: $e")
            throw ResponseStatusException(HttpStatus.CONFLICT, "Invalid request data")
        }
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<User> {
        return try {
            authorizedScope {
                ResponseEntity.ok(userService.deleteUser(userId, authHeader))
            }
        } catch (e: Exception) {
            logger.error("DELETE /api/users/$userId: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find user")
        }
    }


    @GetMapping("/{userId}/chats")
    fun getUserChats(
        @PathVariable userId: Long,
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<Chat>> {
        return try {
            authorizedScope {
                ResponseEntity.ok(userService.getUserChats(userId, searchParams, authHeader))
            }
        } catch (e: Exception) {
            logger.error("GET /api/users/$userId/chats: $e")
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find resource")
        }
    }
}