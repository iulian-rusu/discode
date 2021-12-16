package com.discode.backend.models.requests

class UpdateUserRequest(
    val username: String? = null,
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val description: String? = null,
    val image: ByteArray? = null
)
