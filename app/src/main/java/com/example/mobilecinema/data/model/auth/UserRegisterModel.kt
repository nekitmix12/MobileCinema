package com.example.mobilecinema.data.model.auth

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterModel (

    @SerialName("userName")
    val userLogin :String,

    @SerialName("name")
    val userName: String,

    @SerialName("password")
    val password: String,

    @SerialName("birthDate")
    val  birthDate: Instant,

    @SerialName("gender")
    val gender: Int
)
