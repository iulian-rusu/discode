package com.discode.backend.api.controllers

import com.discode.backend.api.requests.RegisterUserRequest
import com.discode.backend.api.requests.UpdateUserRequest
import com.discode.backend.api.responses.AuthResponse
import com.discode.backend.api.utils.HttpResponse
import com.discode.backend.api.utils.ScopeGuarded
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.User
import com.discode.backend.business.services.interfaces.UserServiceInterface
import com.discode.backend.persistence.query.SearchUserQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController : ScopeGuarded(UserController::class) {
    @Autowired
    private lateinit var userService: UserServiceInterface

    @GetMapping("")
    fun getAllUsers(@RequestParam searchParams: Map<String, String>): ResponseEntity<List<User>> {
        return guardedWith(HttpStatus.BAD_REQUEST, HttpResponse.GENERIC_ERROR_MESSAGE) {
            ResponseEntity.ok(userService.getAllUsers(SearchUserQuery(searchParams)))
        }
    }

    @PostMapping("")
    fun postUser(@RequestBody(required = true) request: RegisterUserRequest): ResponseEntity<AuthResponse> {
        return guardedWith(HttpStatus.CONFLICT, "Conflicting request data") {
            HttpResponse.created(userService.registerUser(request))
        }
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long, @RequestHeader("Authorization") authHeader: String?): ResponseEntity<User> {
        return guardedWith(HttpStatus.NOT_FOUND, "Cannot find user") {
            ResponseEntity.ok(userService.getUser(userId, authHeader))
        }
    }

    @PatchMapping("/{userId}")
    fun patchUser(
        @PathVariable userId: Long,
        @RequestBody(required = true) request: UpdateUserRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<User> {
        return guardedWith(HttpStatus.CONFLICT, "Invalid request data") {
            ResponseEntity.ok(userService.updateUser(userId, request, authHeader))
        }
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: Long,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<User> {
        return guardedWith(HttpStatus.CONFLICT, "Cannot find user") {
            ResponseEntity.ok(userService.deleteUser(userId, authHeader))
        }
    }

    @GetMapping("/{userId}/chats")
    fun getUserChats(
        @PathVariable userId: Long,
        @RequestParam searchParams: Map<String, String>,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<List<Chat>> {
        return guardedWith(HttpStatus.NOT_FOUND, "Cannot find resource") {
            ResponseEntity.ok(userService.getUserChats(userId, searchParams, authHeader))
        }
    }
}