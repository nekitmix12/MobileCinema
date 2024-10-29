package com.example.mobilecinema.data.model.movie

import com.example.mobilecinema.data.model.review.ReviewShortModel
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

    val genres:	List<GenreModel?>,

    val reviews: List<ReviewShortModel?>
)