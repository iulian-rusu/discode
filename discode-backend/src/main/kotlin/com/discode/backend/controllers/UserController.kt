package com.discode.backend.controllers

import com.discode.backend.models.User
import com.discode.backend.models.UserCredentials
import com.discode.backend.models.UserProfile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController {
    @GetMapping("")
    fun getAllUsers(@RequestParam searchParams: Map<String, String>): ResponseEntity<List<User>> = TODO()

    @PostMapping("")
    fun postUser(
        @RequestBody(required = true) credentials: UserCredentials,
        @RequestBody(required = true) profile: UserProfile
    ): ResponseEntity<User> = TODO()

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: Long): ResponseEntity<User> = TODO()

    @PatchMapping("/{userId}")
    fun patchUser(
        @PathVariable userId: Long,
        @RequestBody(required = true) patchedValues: Map<String, String>
    ): ResponseEntity<User> = TODO()

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Unit> = TODO()
}