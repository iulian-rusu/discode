package com.discode.backend.controllers

import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.interfaces.UserInterface
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.persistence.query.SearchUserQuery
import org.springframework.beans.factory.annotation.Autowired
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
    fun getUser(@PathVariable userId: Long) = userService.getUser(userId)

    @PatchMapping("/{userId}")
    fun patchUser(
        @PathVariable userId: Long,
        @RequestBody(required = true) updateRequest: UpdateUserRequest
    ) = userService.patchUser(userId, updateRequest)

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long) = userService.deleteUser(userId)
}