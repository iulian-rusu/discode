package com.discode.backend.business.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SimpleUserDetails(
    val userId: Long,
    val isAdmin: Boolean,
    private val username: String,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
       return if (isAdmin) {
           mutableListOf(SimpleGrantedAuthority("ROLE_ADMIN"))
       } else {
           mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
       }
    }

    override fun getPassword() = ""
    override fun getUsername() = username
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}