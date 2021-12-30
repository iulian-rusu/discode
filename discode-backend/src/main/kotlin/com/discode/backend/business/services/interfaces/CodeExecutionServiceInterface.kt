package com.discode.backend.business.services.interfaces

import com.discode.backend.api.requests.CodeExecutionRequest
import com.discode.backend.business.models.Message

interface CodeExecutionServiceInterface {
    fun execute(request: CodeExecutionRequest, authHeader: String?): Message
}