package com.example.mobilecinema.data.model.movie

import com.example.mobilecinema.data.model.PageInfoModel
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import kotlinx.serialization.Serializable

@Serializable
data class MoviesPagedListModel (

    val movies: MoviesListModel?,

    val pageInfo: PageInfoModel?
)