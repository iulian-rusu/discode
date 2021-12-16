package com.discode.backend.models

data class User(
    val userId: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val description: String?,
    val imagePath: String?
)
