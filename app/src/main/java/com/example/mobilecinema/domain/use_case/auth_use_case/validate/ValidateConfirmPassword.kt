package com.example.mobilecinema.domain.use_case.auth_use_case.validate

class ValidateConfirmPassword {
    fun execute(password: String, confirmPassword: String): ValidationResult {
        if (password!=confirmPassword) {
            return ValidationResult(
                successful = false,
                errorManager = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}