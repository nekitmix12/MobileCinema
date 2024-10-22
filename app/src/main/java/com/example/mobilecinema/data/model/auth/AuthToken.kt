package com.example.mobilecinema.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val token: String
)
