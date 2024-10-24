package com.example.mobilecinema.domain.use_case.auth_use_case

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorManager = " The email can't be blank"
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return ValidationResult(
                successful = false,
                errorManager = "That's not a valid email"
            )
        return ValidationResult(
            successful = true
        )
    }
}