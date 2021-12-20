package com.discode.backend.interfaces

import com.discode.backend.models.Chat
import com.discode.backend.models.User
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.query.SearchUserQuery

interface UserServiceInterface {
    fun getAllUsers(query: SearchUserQuery): List<User>
    fun registerUser(request: RegisterUserRequest): AuthResponse
    fun getUser(userId: Long, authHeader: String?): User
    fun updateUser(userId: Long, request: UpdateUserRequest, authHeader: String?): User
    fun deleteUser(userId: Long, authHeader: String?): User
    fun getUserChats(userId: Long, searchParams: Map<String, String>, authHeader: String?): List<Chat>
}