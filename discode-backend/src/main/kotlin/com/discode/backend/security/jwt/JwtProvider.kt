package com.discode.backend.security.jwt

import com.discode.backend.persistence.UserRepository
import com.discode.backend.security.SimpleUserDetails
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest


@Component
class JwtProvider {
    @Autowired
    lateinit var jwtProperties: JwtProperties

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    fun createToken(userId: Long): String {
        val isAdmin = userRepository.isAdmin(userId)
        val claims = Jwts.claims()
        claims["userId"] = userId
        claims["isAdmin"] = isAdmin

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .compact()
    }

    fun getUserDetails(token: String): SimpleUserDetails {
        val user = userRepository.findOne(getUserId(token))
        return userDetailsService.loadUserByUsername(user.username) as SimpleUserDetails
    }
    fun getAuthentication(token: String): Authentication {
        val userDetails = getUserDetails(token)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserId(token: String) =
        (Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token).body["userId"] as Int).toLong()

    fun getToken(authHeader: String?): String? {
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authHeader.split(" ")[1]
        } else null
    }

    fun getToken(req: HttpServletRequest): String? {
        val authHeader = req.getHeader("Authorization")
        return getToken(authHeader)
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token)
            claims.signature.isNotEmpty()
        } catch (e: Exception) {
            throw JwtAuthenticationException(e.message ?: "JWT validation exception")
        }
    }
}