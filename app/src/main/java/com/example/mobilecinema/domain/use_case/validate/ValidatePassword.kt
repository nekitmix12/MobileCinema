package com.example.mobilecinema.domain.use_case.validate

import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorManager = StringHelper().getString(R.string.password_small)
            )
        }
        val containsLettersAndDigits = password.any{ it.isDigit() || it.isLetter()}
        if (!containsLettersAndDigits)
            return ValidationResult(
                successful = false,
                errorManager = StringHelper().getString(R.string.name_validation_error)
            )
        return ValidationResult(
            successful = true
        )
    }
}