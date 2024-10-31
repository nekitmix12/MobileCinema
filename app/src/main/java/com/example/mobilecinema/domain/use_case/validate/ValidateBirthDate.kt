package com.example.mobilecinema.domain.use_case.validate

class ValidateBirthDate {
    fun execute(date: String): ValidationResult {
        val pattern = "(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(19|20)\\d{2}".toRegex()
        if (!date.matches(pattern)) {
            return ValidationResult(
                successful = false,
                errorManager = "The date is not correct"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}