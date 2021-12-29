package com.discode.backend.api.requests.external

data class GodboltCodeExecutionRequest(
    val source: String,
    val compiler: String,
    val lang: String,
    val options: GodboltRequestOptions = GodboltRequestOptions()
)
