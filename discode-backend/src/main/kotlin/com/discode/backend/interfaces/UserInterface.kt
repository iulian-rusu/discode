package com.discode.backend.interfaces

import com.discode.backend.models.User
import com.discode.backend.models.UserCredentials
import com.discode.backend.models.UserProfile
import com.discode.backend.models.RegistrationResponse
import com.discode.backend.persistence.query.UserSearchQuery
import org.springframework.http.ResponseEntity

interface UserInterface {
    fun getAllUsers(query: UserSearchQuery): ResponseEntity<List<User>>
    fun postUser(credentials: UserCredentials, profile: UserProfile): ResponseEntity<RegistrationResponse>
    fun getUser(userId: Long): ResponseEntity<User>
    fun patchUser(userId: Long, patchedValues: MutableMap<String, String>): ResponseEntity<User>
    fun deleteUser(userId: Long): ResponseEntity<User>
}