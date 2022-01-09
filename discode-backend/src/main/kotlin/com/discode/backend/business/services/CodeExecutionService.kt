package com.discode.backend.business.services

import com.discode.backend.api.requests.CodeExecutionRequest
import com.discode.backend.api.requests.external.GodboltCodeExecutionRequest
import com.discode.backend.business.models.Language
import com.discode.backend.business.models.Message
import com.discode.backend.business.security.jwt.JwtAuthorized
import com.discode.backend.business.services.interfaces.CodeExecutionServiceInterface
import com.discode.backend.persistence.ChatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.server.ResponseStatusException

@Service
class CodeExecutionService : JwtAuthorized(), CodeExecutionServiceInterface {
    companion object {
        const val CODE_EXECUTION_ENDPOINT = "https://godbolt.org/api/compiler"
    }

    @Autowired
    private lateinit var chatRepository: ChatRepository

    override fun execute(request: CodeExecutionRequest, authHeader: String?): Message {
        val message = chatRepository.findMessage(request.messageId)
        return ifAuthorized(
            header = authHeader,
            authorizer = { details ->
                chatRepository.isMember(message.author.chatId, details.userId)
            },
            action = {
                message.codeOutput = sendRequest(message, request.language)
                chatRepository.updateCodeOutput(message)
            }
        )
    }

    private fun sendRequest(message: Message, language: Language): String? {
        val restTemplate = RestTemplate()
        val uri = "$CODE_EXECUTION_ENDPOINT/${language.compiler}/compile"
        val sourceCode = message.content.let { content ->
            if (content.length > 2 && content.first() == '`' && content.last() == '`')
                content.substring(1, content.lastIndex)
            else
                content
        }

        val request = GodboltCodeExecutionRequest(
            source = sourceCode,
            compiler = language.compiler,
            lang = language.id
        )
        val response = restTemplate.postForEntity(uri, request, String::class.java)
        if (response.statusCode != HttpStatus.OK) {
            throw ResponseStatusException(response.statusCode, "Cannot execute code")
        }
        return response.body?.let {
            it.substring(it.indexOf("\n\n") + 2)
        }
    }
}