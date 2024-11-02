package com.example.mobilecinema.data.model.auth

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserShortModel (
    val userId: String,

    val nickName: String?,

    val avatar: String?

)