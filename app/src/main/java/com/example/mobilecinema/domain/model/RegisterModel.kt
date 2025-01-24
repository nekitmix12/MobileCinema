package com.example.mobilecinema.domain.model

data class RegisterModel (
    val loginError:String?=null,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val birthDateError: String? = null,
    val genderError: String? = null
    )