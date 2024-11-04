package com.example.mobilecinema.data.model.movie

import com.example.mobilecinema.data.model.review.ReviewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsModel (
    val id: String,
    val name: String?,
    val poster:	String?,
    val year: Int,
    val country: String?,
    val genres:	List<GenreModel?>,
    val reviews: List<ReviewModel>?,
    @SerialName("time")
    val filmTime: Int,
    val tagline: String?,
    val description: String?,
    val director: String,
    val budget: Int?,
    val fees: Int?,
    val ageLimit: Int
)