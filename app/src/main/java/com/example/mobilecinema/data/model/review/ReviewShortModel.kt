package com.example.mobilecinema.data.model.review

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ReviewShortModel (

    val id: String,
    val rating: Int
){
    init{
        require(try {
            UUID.fromString(id)
            true
        } catch (e: IllegalArgumentException) {
            false
        })
    }
}