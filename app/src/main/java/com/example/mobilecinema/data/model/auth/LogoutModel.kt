package com.example.mobilecinema.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogoutModel(
    val token: String,
    val message: String
)

