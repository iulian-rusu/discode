package com.discode.backend.api.controllers

import com.discode.backend.api.requests.CodeExecutionRequest
import com.discode.backend.api.responses.LanguageResponseEntry
import com.discode.backend.api.utils.HttpResponse
import com.discode.backend.api.utils.ScopeGuarded
import com.discode.backend.business.models.Language
import com.discode.backend.business.models.Message
import com.discode.backend.business.services.interfaces.CodeExecutionServiceInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/code-execution")
class CodeExecutionController : ScopeGuarded(CodeExecutionController::class) {
    companion object {
        val languageList = Language.values().map { lang ->
            LanguageResponseEntry(
                name = lang,
                displayName = lang.id.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            )
        }
    }

    @Autowired
    private lateinit var codeExecutionService: CodeExecutionServiceInterface

    @GetMapping("/languages")
    fun getLanguages(): ResponseEntity<List<LanguageResponseEntry>> {
        return ResponseEntity.ok(languageList)
    }

    @PostMapping("")
    fun postCodeExecution(
        @RequestBody request: CodeExecutionRequest,
        @RequestHeader("Authorization") authHeader: String?
    ): ResponseEntity<Message> {
        return guardedWith(HttpStatus.NOT_ACCEPTABLE, HttpResponse.GENERIC_ERROR_MESSAGE) {
            ResponseEntity.ok(codeExecutionService.execute(request, authHeader))
        }
    }
}