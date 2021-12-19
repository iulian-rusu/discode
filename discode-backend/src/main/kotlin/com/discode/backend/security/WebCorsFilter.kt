package com.discode.backend.security

import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


class WebCorsFilter : CorsFilter(configurationSource()) {
    companion object {
        fun configurationSource(): UrlBasedCorsConfigurationSource {
            val configuration = CorsConfiguration()
            configuration.allowCredentials = false
            configuration.addAllowedOrigin("*")
            configuration.addAllowedHeader("*")
            configuration.maxAge = 3600L
            val corsConfigurationSource = UrlBasedCorsConfigurationSource()
            corsConfigurationSource.registerCorsConfiguration("/**", configuration)
            return corsConfigurationSource
        }
    }
}
