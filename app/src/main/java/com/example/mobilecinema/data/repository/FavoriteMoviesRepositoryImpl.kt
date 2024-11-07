package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.FavoritesMoviesRemoteDataSource
import com.example.mobilecinema.data.datasource.remote.data_source.FavoritesMoviesRemoteDataSourceImpl
import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.domain.repository.FavoriteMoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteMoviesRepositoryImpl(private val favoritesMoviesRemoteDataSource: FavoritesMoviesRemoteDataSource): FavoriteMoviesRepository {
    override fun getFavorites(): Flow<MoviesListModel> {
        return favoritesMoviesRemoteDataSource.getFavorites()
    }

    override suspend fun postFavorites(id: String) {
        favoritesMoviesRemoteDataSource.postFavorites(id)
    }

    override suspend fun deleteFavorites(id: String) {
        favoritesMoviesRemoteDataSource.deleteFavorites(id)
    }
}