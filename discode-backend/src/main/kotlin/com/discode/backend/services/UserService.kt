package com.discode.backend.services

import com.discode.backend.interfaces.ImageStorageInterface
import com.discode.backend.interfaces.UserInterface
import com.discode.backend.models.User
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.UserRepository
import com.discode.backend.persistence.mappers.UserRowMapper
import com.discode.backend.persistence.query.SearchUserQuery
import com.discode.backend.persistence.query.UpdateUserQuery
import com.discode.backend.security.Encoder
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    override fun getAllUsers(query: SearchUserQuery): ResponseEntity<List<User>> =
        try {
            genericQueryRepository.find(query, UserRowMapper())
                .toList()
                .let { ResponseEntity.ok(it) }
        } catch (e: Exception) {
            logger.error("getAllUsers(): $e")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

    override fun postUser(request: RegisterUserRequest): ResponseEntity<AuthResponse> =
        try {
            val newUser = userRepository.save(request)
            ResponseEntity.ok(AuthResponse("jwt", newUser))
        } catch (e: Exception) {
            logger.error("postUser(username=${request.username}): $e")
            ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

    override fun getUser(userId: Long): ResponseEntity<User> =
        try {
            ResponseEntity.ok(userRepository.findOne(userId))
        } catch (e: Exception) {
            logger.error("getUser(userId=$userId): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

    override fun patchUser(userId: Long, updateRequest: UpdateUserRequest): ResponseEntity<User> =
        try {
            val query = toUpdateQuery(userId, updateRequest)
            genericQueryRepository.execute(query)
            ResponseEntity.ok(userRepository.findOne(userId))
        } catch (e: Exception) {
            logger.error("patchUser(userId=$userId): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

    override fun deleteUser(userId: Long): ResponseEntity<User> =
        try {
            val toDelete = userRepository.findOne(userId)
            userRepository.deleteOne(toDelete.userId)
            ResponseEntity.ok(toDelete)
        } catch (e: Exception) {
            logger.error("deleteUser(userId=$userId): $e")
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

    private fun toUpdateQuery(userId: Long, updateRequest: UpdateUserRequest): UpdateUserQuery {
        val imagePath = updateRequest.image?.let {
            if (it.isNotEmpty()) {
                imageStorage.saveImage(userId, it)
            } else {
                imageStorage.deleteImage(imageStorage.imagePathFor(userId))
                ""
            }
        }

        val passwordHash = updateRequest.password?.let {
            Encoder.hashString(it)
        }

        return UpdateUserQuery(
            userId = userId,
            username = updateRequest.username,
            passwordHash = passwordHash,
            firstName = updateRequest.firstName,
            lastName = updateRequest.lastName,
            email = updateRequest.email,
            description = updateRequest.description,
            imagePath = imagePath
        )
    }
}