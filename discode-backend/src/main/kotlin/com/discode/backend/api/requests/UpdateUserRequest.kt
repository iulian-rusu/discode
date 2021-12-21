package com.discode.backend.api.requests

class UpdateUserRequest(
    val password: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val description: String? = null,
    val image: ByteArray? = null
)
