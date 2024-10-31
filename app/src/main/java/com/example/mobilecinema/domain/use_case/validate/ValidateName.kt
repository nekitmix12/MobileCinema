package com.example.mobilecinema.domain.use_case.validate


class ValidateName {
    fun execute(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorManager = " The name can't be blank"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}