package com.discode.backend.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object Encoder {
    private val encoder = BCryptPasswordEncoder()

    fun hashString(input: String): String = encoder.encode(input)
}
