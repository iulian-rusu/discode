package com.discode.backend.security

import org.springframework.stereotype.Component
import javax.servlet.annotation.WebFilter
import javax.servlet.*
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebFilter("/api/*")
@Component
class WebResponseHeaderFilter : HttpFilter() {
    companion object {
        const val CORS_HEADER = "Access-Control-Allow-Origin"
    }

    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        response?.setHeader(CORS_HEADER, "*")
        chain?.doFilter(request, response)
    }
}
