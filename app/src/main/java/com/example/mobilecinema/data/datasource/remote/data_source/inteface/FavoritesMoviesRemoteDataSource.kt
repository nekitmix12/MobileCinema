package com.example.mobilecinema.data.datasource.remote.data_source.inteface

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import kotlinx.coroutines.flow.Flow

interface FavoritesMoviesRemoteDataSource {
    fun getFavorites(): Flow<MoviesListModel>

    suspend fun postFavorites(id: String)

    suspend fun deleteFavorites(id: String)
}