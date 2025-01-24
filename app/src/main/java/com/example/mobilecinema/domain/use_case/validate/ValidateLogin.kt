package com.example.mobilecinema.domain.use_case.validate

import android.util.Patterns
import com.example.mobilecinema.R
import com.example.mobilecinema.di.MainContext

class ValidateLogin {
    fun execute(login: String?): ValidationResult {
        if (login?.isBlank() == true) {
            return ValidationResult(
                successful = false,
                errorManager = MainContext.provideInstance().provideContext().getString(R.string.login_error_null)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}