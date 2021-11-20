package com.discode.backend.services

import com.discode.backend.interfaces.ImageStorageInterface
import com.discode.backend.interfaces.UserInterface
import com.discode.backend.models.User
import com.discode.backend.models.UserCredentials
import com.discode.backend.models.UserProfile
import com.discode.backend.models.RegistrationResponse
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.UserRepository
import com.discode.backend.persistence.mappers.UserRowMapper
import com.discode.backend.persistence.query.UserSearchQuery
import com.discode.backend.persistence.query.UserUpdateQuery
import com.discode.backend.security.Encoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserService : UserInterface {
    @Autowired
    private lateinit var genericQueryRepository: GenericQueryRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var imageStorage: ImageStorageInterface

    override fun getAllUsers(query: UserSearchQuery): ResponseEntity<List<User>> =
        try {
            genericQueryRepository.find(query, UserRowMapper())
                .let { users ->
                    ResponseEntity.ok(users.toList())
                }
        } catch (e: Exception) {
            println(e)
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    override fun postUser(credentials: UserCredentials, profile: UserProfile): ResponseEntity<RegistrationResponse> =
        try {
            val id = userRepository.save(credentials, profile)
            ResponseEntity.ok(RegistrationResponse(id, User(credentials.username, profile)))
        } catch (e: Exception) {
            println(e)
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

    override fun getUser(userId: Long): ResponseEntity<User> =
        try {
            ResponseEntity.ok(userRepository.findOne(userId))
        } catch (e: Exception) {
            println(e)
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

    override fun patchUser(userId: Long, patchedValues: MutableMap<String, String>): ResponseEntity<User> =
        try {
            val image = patchedValues.getOrDefault("image", null)
            patchedValues["imagePath"] = image?.let {
                imageStorage.saveImage(it.toByteArray())
            } ?: ""

            val password = patchedValues.getOrDefault("password", null)
            patchedValues["passwordHash"] = password?.let {
                Encoder.hashString(it)
            } ?: ""

            val query = UserUpdateQuery(userId, patchedValues)
            genericQueryRepository.execute(query)
            ResponseEntity.ok(userRepository.findOne(query.userId))
        } catch (e: Exception) {
            println(e)
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

    override fun deleteUser(userId: Long): ResponseEntity<User> =
        try {
            val toDelete = userRepository.findOne(userId)
            userRepository.deleteOne(userId)
            ResponseEntity.ok(toDelete)
        } catch (e: Exception) {
            println(e)
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
}