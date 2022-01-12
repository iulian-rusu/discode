package com.discode.backend.api.responses

import com.discode.backend.business.models.Language

data class NamedLanguage (
    val name: Language,
    val displayName: String
)