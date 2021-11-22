package com.discode.backend.controllers

import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.interfaces.UserInterface
import com.discode.backend.models.User
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.persistence.query.SearchUserQuery
import com.discode.backend.security.jwt.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController {
    @Autowired
    private lateinit var userService: UserInterface

    @GetMapping("")
    fun getAllUsers(@RequestParam searchParams: Map<String, String>) =
        userService.getAllUsers(SearchUserQuery(searchParams))

    @PostMapping("")
    fun postUser(@RequestBody(required = true) request: RegisterUserRequest) = userService.postUser(request)

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long, @RequestHeader("Authorization") authHeader: String?) =
        userService.getUser(userId, authHeader)

    @PatchMapping("/{userId}")
    fun patchUser(
        @PathVariable userId: Long,
        @RequestBody(required = true) updateRequest: UpdateUserRequest,
        @RequestHeader("Authorization") authHeader: String?
    ) = userService.patchUser(userId, updateRequest, authHeader)

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long, @RequestHeader("Authorization") authHeader: String?) =
        userService.deleteUser(userId, authHeader)
}