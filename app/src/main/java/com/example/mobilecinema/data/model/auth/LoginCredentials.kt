package com.example.mobilecinema.data.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials (

    @SerialName("username")
    val username: String?,

    @SerialName("password")
    val password: String?
)