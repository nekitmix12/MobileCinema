package com.example.mobilecinema.presentation.login

sealed class RegistrationFormEvent {
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationFormEvent()
    data class BirthDateChanged(val birthDate: String) : RegistrationFormEvent()
    data class NameChanged(val name: String) : RegistrationFormEvent()
    data class LoginChanged(val login: String) : RegistrationFormEvent()
    data class GenderChanged(val gender: Int): RegistrationFormEvent()
    object Submit: RegistrationFormEvent()
}