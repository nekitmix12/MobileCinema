package com.example.mobilecinema.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class Gender(sex: Int) {
    MALE(sex = 1),
    FEMALE(sex = 2)
}