package com.discode.backend.persistence.query

interface QueryCriteria {
    fun getQuery(): String
    fun getParams(): Map<String, Any>
}