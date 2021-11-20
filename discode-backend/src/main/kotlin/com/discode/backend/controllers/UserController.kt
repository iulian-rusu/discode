package com.discode.backend.controllers

import com.discode.backend.models.RegistrationRequest
import com.discode.backend.interfaces.UserInterface
import com.discode.backend.persistence.query.UserSearchQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController {
    @Autowired
    private lateinit var userService: UserInterface

    @GetMapping("")
    fun getAllUsers(@RequestParam searchParams: Map<String, String>) =
        userService.getAllUsers(UserSearchQuery(searchParams))

    @PostMapping("")
    fun postUser(
        @RequestBody(required = true) req: RegistrationRequest
    ) = userService.postUser(req.credentials, req.profile)

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long) = userService.getUser(userId)

    @PatchMapping("/{userId}")
    fun patchUser(
        @PathVariable userId: Long,
        @RequestBody(required = true) patchedValues: MutableMap<String, String>
    ) = userService.patchUser(userId, patchedValues)

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long) = userService.deleteUser(userId)
}