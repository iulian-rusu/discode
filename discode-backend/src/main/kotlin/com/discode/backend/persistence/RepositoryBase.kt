package com.discode.backend.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

abstract class RepositoryBase {
    @Autowired
    protected lateinit var jdbcTemplate: NamedParameterJdbcTemplate
}