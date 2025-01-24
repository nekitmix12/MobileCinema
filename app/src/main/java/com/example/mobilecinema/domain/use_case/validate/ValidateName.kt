package com.example.mobilecinema.domain.use_case.validate

import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper


class ValidateName {
    fun execute(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult(
                successful = false,
                errorManager = StringHelper().getString(R.string.name_empty)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}