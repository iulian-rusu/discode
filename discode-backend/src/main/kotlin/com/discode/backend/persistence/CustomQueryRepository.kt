package com.discode.backend.persistence

import com.discode.backend.persistence.query.QueryCriteria
import org.springframework.jdbc.core.RowMapper

class CustomQueryRepository : NamedParameterBase() {
    fun <T, Mapper> findByCriteria(criteria: QueryCriteria, mapper: Mapper): List<T>
            where Mapper : RowMapper<T> = jdbcTemplate.query(criteria.getQuery(), criteria.getParams(), mapper)

    fun executeByCriteria(criteria: QueryCriteria) =
        jdbcTemplate.update(criteria.getQuery(), criteria.getParams())
}