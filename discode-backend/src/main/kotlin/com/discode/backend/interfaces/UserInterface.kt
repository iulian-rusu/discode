package com.discode.backend.interfaces

import com.discode.backend.models.User
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.query.SearchUserQuery
import org.springframework.http.ResponseEntity

interface UserInterface {
    fun getAllUsers(query: SearchUserQuery): ResponseEntity<List<User>>
    fun postUser(request: RegisterUserRequest): ResponseEntity<AuthResponse>
    fun getUser(userId: Long): ResponseEntity<User>
    fun patchUser(userId: Long, updateRequest: UpdateUserRequest): ResponseEntity<User>
    fun deleteUser(userId: Long): ResponseEntity<User>
}