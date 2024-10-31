package com.example.mobilecinema.domain.use_case.validate

import android.util.Patterns

class ValidateLogin {
    fun execute(login: String): ValidationResult {
        if (login.isBlank()) {
            return ValidationResult(
                successful = false,
                errorManager = " The login can't be blank"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}