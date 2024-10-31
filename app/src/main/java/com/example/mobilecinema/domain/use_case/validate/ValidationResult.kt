package com.example.mobilecinema.domain.use_case.validate

data class ValidationResult(
    val successful:Boolean,
    val errorManager: String? = null
)