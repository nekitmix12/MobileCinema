package com.example.mobilecinema.domain.use_case.validate

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorManager = "The password needs to consist of a least 8 characters"
            )
        }
        val containsLettersAndDigits = password.any{ it.isDigit() || it.isLetter()}
        if (!containsLettersAndDigits)
            return ValidationResult(
                successful = false,
                errorManager = "The password needs to consist at least on letter and digit"
            )
        return ValidationResult(
            successful = true
        )
    }
}