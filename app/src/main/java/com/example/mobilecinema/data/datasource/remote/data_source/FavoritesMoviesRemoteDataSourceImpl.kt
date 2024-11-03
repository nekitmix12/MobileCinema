package com.example.mobilecinema.data.datasource.remote.data_source

import android.util.Log
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceFavoriteMovies
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavoritesMoviesRemoteDataSourceImpl(private val apiServiceFavoriteMovies: ApiServiceFavoriteMovies) :
    FavoritesMoviesRemoteDataSource {
    override fun getFavorites(): Flow<MoviesListModel> {
        return flow {
            emit(apiServiceFavoriteMovies.getFavorites())
        }.map {
            MoviesListModel(it.movies)
        }
    }

    override suspend fun postFavorites(id: String) {
        apiServiceFavoriteMovies.postFavorites(id)
    }

    override suspend fun deleteFavorites(id: String) {
            apiServiceFavoriteMovies.deleteFavorites(id)
    }
}