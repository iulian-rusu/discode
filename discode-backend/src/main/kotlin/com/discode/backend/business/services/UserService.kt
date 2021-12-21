package com.discode.backend.business.services

import com.discode.backend.api.requests.RegisterUserRequest
import com.discode.backend.api.requests.UpdateUserRequest
import com.discode.backend.api.responses.AuthResponse
import com.discode.backend.business.interfaces.ImageServiceInterface
import com.discode.backend.business.interfaces.UserServiceInterface
import com.discode.backend.business.models.Chat
import com.discode.backend.business.models.User
import com.discode.backend.business.security.Encoder
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.persistence.GenericQueryRepository
import com.discode.backend.persistence.UserRepository
import com.discode.backend.persistence.mappers.ChatRowMapper
import com.discode.backend.persistence.mappers.UserRowMapper
import com.discode.backend.persistence.query.SearchChatQuery
import com.discode.backend.persistence.query.SearchUserQuery
import com.discode.backend.persistence.query.UpdateUserQuery
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

    override fun registerUser(request: RegisterUserRequest): AuthResponse {
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

    override fun updateUser(userId: Long, request: UpdateUserRequest, authHeader: String?): User {
        return ifAuthorizedAs(userId, authHeader) {
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
        return ifAuthorizedAs(userId, authHeader) {
            val toDelete = userRepository.findOne(userId)
            userRepository.deleteOne(toDelete.userId)
            toDelete
        }
    }

    override fun getUserChats(userId: Long, searchParams: Map<String, String>, authHeader: String?): List<Chat> {
        return ifAuthorized(
            header = authHeader,
            authorizer = { details ->
                details.userId == userId || details.isAdmin
            },
            action = {
                val query = SearchChatQuery(userId, searchParams)
                genericQueryRepository.find(query, ChatRowMapper())
                    .toList()
            }
        )
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