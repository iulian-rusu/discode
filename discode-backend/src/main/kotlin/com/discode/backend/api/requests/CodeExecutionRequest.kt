package com.discode.backend.api.requests

import com.discode.backend.business.models.Language

data class CodeExecutionRequest(
    val messageId: Long,
    val language: Language
)
