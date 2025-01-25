package com.example.mobilecinema.data.model.movie

import com.example.mobilecinema.presentation.adapter.Item
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GenreModel(

    @SerialName("id")
    val id:String,

    @SerialName("name")
    val genreName:String?

) : Item