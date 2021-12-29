package com.discode.backend.api.requests.external

data class GodboltRequestOptions(
    val userArguments: String = "",
    val compilerOptions: GodboltCompilerOptions = GodboltCompilerOptions(),
    val filters: GodboltFilters = GodboltFilters(),
)