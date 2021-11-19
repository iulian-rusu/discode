package com.discode.backend.persistence.query

interface ParametrizedQuery {
    fun getSql(): String
    fun getParams(): Map<String, Any>
}