package com.example.mobilecinema.domain.model


data class UserRegisterModel (
    val userName:String,
    val name:String,
    val password:String,
    val email:String,
    val birthDate: String?,
    val gender: Gender?
)