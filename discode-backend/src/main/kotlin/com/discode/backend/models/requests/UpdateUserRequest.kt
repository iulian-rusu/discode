package com.discode.backend.models.requests

class UpdateUserRequest(
    val username: String = "",
    val password: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val description: String = "",
    val image: ByteArray? = null
)
