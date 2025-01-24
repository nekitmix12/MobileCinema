package com.example.mobilecinema.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
enum class Gender(sex: Int) {
    MALE(1),
    FEMALE(0)
}