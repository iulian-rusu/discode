package com.discode.backend.models

data class UserProfile (
    val firstName: String,
    val lastName: String,
    val email: String,
    val description: String,
    val imagePath: String
)
