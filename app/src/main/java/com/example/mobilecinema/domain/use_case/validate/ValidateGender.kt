package com.example.mobilecinema.domain.use_case.validate

import android.util.Patterns

class ValidateGender {
    fun execute(gender: Int): ValidationResult {
        /*if (gender==-1) {
            return ValidationResult(
                successful = false,
                errorManager = " The gender can't be blank"
            )
        }*/
        return ValidationResult(
            successful = true
        )
    }
}