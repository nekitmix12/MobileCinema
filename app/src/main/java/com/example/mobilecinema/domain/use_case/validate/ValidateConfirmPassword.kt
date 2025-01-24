package com.example.mobilecinema.domain.use_case.validate

import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper

class ValidateConfirmPassword {
    fun execute(password: String?, confirmPassword: String): ValidationResult {

        if (password!=confirmPassword) {
            return ValidationResult(
                successful = false,
                errorManager = StringHelper().getString(R.string.password_not_same)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}