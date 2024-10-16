package com.example.mobilecinema.data.model.movie

import com.example.mobilecinema.data.model.GenreModel
import com.example.mobilecinema.data.model.ReviewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class MovieDetailsModel (
    val id: String,
    val name: String?,
    val poster:	String?,
    val year: Int,
    val country: String?,
    val genres:	GenreModel?,
    val reviews: ReviewModel?,
    @SerialName("time")
    val filmTime: Int,
    val tagline: String?,
    val description: String?,
    val director: String,
    val budget: Int?,
    val fees: Int?,
    val ageLimit: Int
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