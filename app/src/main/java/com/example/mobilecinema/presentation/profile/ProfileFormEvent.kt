package com.example.mobilecinema.presentation.profile

import com.example.mobilecinema.presentation.login.RegistrationFormEvent

sealed class ProfileFormEvent {
    data class EmailChanged(val email: String) : ProfileFormEvent()
    data class BirthDateChanged(val birthDate: String) : ProfileFormEvent()
    data class NameChanged(val name: String) : ProfileFormEvent()
    data class LoginChanged(val login: String) : ProfileFormEvent()
    data class GenderChanged(val gender: Int): ProfileFormEvent()
    object Submit: ProfileFormEvent()
}