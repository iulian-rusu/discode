package com.discode.backend.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) : GenericFilterBean() {
    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        try {
            val token = jwtProvider.getToken(req as HttpServletRequest)
            if (token != null && jwtProvider.validateToken(token)) {
                val auth = jwtProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            filterChain.doFilter(req, res)

        } catch (e: Exception) {
            logger.error("JWT filter error: $e")
            res as HttpServletResponse
            val text = "Invalid JWT"
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, text)
            res.characterEncoding = "UTF-8"
            res.writer.print(text)
            res.writer.close()
        }
    }
}