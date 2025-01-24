package com.example.mobilecinema.domain.model

data class ProfileModel(
    val id:String,
    val nickName:String?,
    val email:String?,
    val avatarLink:String?,
    val name:String,
    val birthDate: String,
    val gender: Gender
)
