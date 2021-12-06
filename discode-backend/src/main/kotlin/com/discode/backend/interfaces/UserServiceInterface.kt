package com.discode.backend.interfaces

import com.discode.backend.models.Chat
import com.discode.backend.models.User
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.query.SearchUserQuery
import org.springframework.http.ResponseEntity

interface UserServiceInterface {
    fun getAllUsers(query: SearchUserQuery): ResponseEntity<List<User>>
    fun postUser(request: RegisterUserRequest): ResponseEntity<AuthResponse>
    fun getUser(userId: Long, authHeader: String?): ResponseEntity<User>
    fun patchUser(userId: Long, request: UpdateUserRequest, authHeader: String?): ResponseEntity<User>
    fun deleteUser(userId: Long, authHeader: String?): ResponseEntity<User>
    fun getUserChats(userId: Long, searchParams: Map<String, String>, authHeader: String?): ResponseEntity<List<Chat>>
}