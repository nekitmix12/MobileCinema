package com.example.mobilecinema.domain.model

data class ProfileModel(
    val login: String? = "",
    val loginError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val birthDate: String = "",
    val birthDateError: String? = null,
    val gender: Int = 0,
    val genderError: String? = null,
    val id: String = "",
    val avatarLink: String? = null,
    val avatarLinkError: String? = null
)