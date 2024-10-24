package com.example.mobilecinema.presentation

data class RegisterModel (
    val login: String = "",
    val loginError:String?=null,
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password:String = "",
    val passwordError: String? = null,
    val confirmPassword:String = "",
    val confirmPasswordError: String? = null,
    val birthDate:String = "",
    val birthDateError: String? = null,
    val gender: Int = 0,
    val genderError: String? = null
    )