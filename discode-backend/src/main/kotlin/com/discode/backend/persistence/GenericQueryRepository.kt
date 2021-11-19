package com.discode.backend.persistence

import com.discode.backend.persistence.query.ParametrizedQuery
import org.springframework.jdbc.core.RowMapper

class GenericQueryRepository : RepositoryBase() {
    fun <T, Mapper> find(query: ParametrizedQuery, mapper: Mapper): List<T>
            where Mapper : RowMapper<T> = jdbcTemplate.query(query.getSql(), query.getParams(), mapper)

    fun execute(query: ParametrizedQuery) =
        jdbcTemplate.update(query.getSql(), query.getParams())
}