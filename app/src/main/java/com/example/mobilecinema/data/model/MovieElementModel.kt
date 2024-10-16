package com.example.mobilecinema.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class MovieElementModel (

    val id: String,

    @SerialName("name")
    val moveName: String?,

    val poster:	String?,

    val year: Int,

    val country: String?,

    val genres:	GenreModel?,

    val reviews: ReviewShortModel?
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