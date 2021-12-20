package com.discode.backend.services

import com.discode.backend.interfaces.ImageServiceInterface
import com.discode.backend.interfaces.UserServiceInterface
import com.discode.backend.models.Chat
import com.discode.backend.models.User
import com.discode.backend.models.requests.RegisterUserRequest
import com.discode.backend.models.requests.UpdateUserRequest
import com.discode.backend.models.responses.AuthResponse
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.UserRepository
import com.discode.backend.persistence.mappers.ChatRowMapper
import com.discode.backend.persistence.mappers.UserRowMapper
import com.discode.backend.persistence.query.SearchChatQuery
import com.discode.backend.persistence.query.SearchUserQuery
import com.discode.backend.persistence.query.UpdateUserQuery
import com.discode.backend.security.Encoder
import com.discode.backend.security.jwt.JwtAuthorized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService : JwtAuthorized(), UserServiceInterface {
    @Autowired
    private lateinit var genericQueryRepository: GenericQueryRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var imageStorage: ImageServiceInterface

    override fun getAllUsers(query: SearchUserQuery): List<User> {
        return genericQueryRepository.find(query, UserRowMapper()).toList()
    }

    override fun postUser(request: RegisterUserRequest): AuthResponse {
        if (!isValid(request)) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "Invalid request data")
        }

        val newUser = userRepository.save(request)
        val jwt = jwtProvider.createToken(newUser.userId)
        return AuthResponse(jwt, newUser)
    }

    override fun getUser(userId: Long, authHeader: String?): User {
        return userRepository.findOne(userId)
    }

    override fun patchUser(userId: Long, request: UpdateUserRequest, authHeader: String?): User {
        return ifAuthorizedOn(userId, authHeader) {
            val query = toUpdateQuery(userId, request)
            genericQueryRepository.execute(query)
            try {
                userRepository.findOne(userId)
            } catch (e: Exception) {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            }
        }
    }

    override fun deleteUser(userId: Long, authHeader: String?): User {
        return ifAuthorizedOn(userId, authHeader) {
            val toDelete = userRepository.findOne(userId)
            userRepository.deleteOne(toDelete.userId)
            toDelete
        }
    }

    override fun getUserChats(userId: Long, searchParams: Map<String, String>, authHeader: String?): List<Chat> {
        return ifAuthorizedOn(userId, authHeader) {
            val query = SearchChatQuery(userId, searchParams)
            genericQueryRepository.find(query, ChatRowMapper())
                .toList()
        }
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

    private fun isValid(request: RegisterUserRequest): Boolean {
        return request.password.length >= 8 && request.username.length >= 3
                && request.firstName.length >= 2 && request.lastName.length >= 2
    }
}