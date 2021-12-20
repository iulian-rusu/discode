package com.discode.backend.security.jwt

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException(val status: HttpStatus, message: String): AuthenticationException(message)