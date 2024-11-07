package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import kotlinx.coroutines.flow.Flow

interface FavoriteMoviesRepository {
    fun getFavorites(): Flow<MoviesListModel>

    suspend fun postFavorites(id: String)

    suspend fun deleteFavorites(id: String)
}