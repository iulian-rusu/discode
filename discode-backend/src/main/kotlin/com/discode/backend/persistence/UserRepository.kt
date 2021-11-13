package com.discode.backend.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    @Autowired
    private lateinit var jdbcTemplate: NamedParameterJdbcTemplate
}