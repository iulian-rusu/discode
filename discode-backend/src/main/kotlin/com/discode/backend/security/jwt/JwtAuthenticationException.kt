package com.discode.backend.security.jwt

import org.springframework.security.core.AuthenticationException

class JwtAuthenticationException(msg: String) : AuthenticationException(msg)