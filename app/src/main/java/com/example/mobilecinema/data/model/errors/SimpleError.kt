package com.example.mobilecinema.data.model.errors

import kotlinx.serialization.Serializable

@Serializable
data class SimpleError(
    val exception: String?,
    val errorMessage: String,
)