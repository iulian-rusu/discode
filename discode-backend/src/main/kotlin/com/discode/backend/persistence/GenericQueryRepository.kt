package com.discode.backend.persistence

import com.discode.backend.persistence.query.ParametrizedQuery
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class GenericQueryRepository : RepositoryBase() {
    fun <T, Mapper> find(query: ParametrizedQuery, mapper: Mapper): List<T>
            where Mapper : RowMapper<T> = namedJdbcTemplate.query(query.getSql(), query.params, mapper)

    fun execute(query: ParametrizedQuery) =
        namedJdbcTemplate.update(query.getSql(), query.params)
}