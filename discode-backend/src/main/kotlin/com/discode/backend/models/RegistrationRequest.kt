package com.discode.backend.models


data class RegistrationRequest(
    val credentials: UserCredentials,
    val profile: UserProfile
)