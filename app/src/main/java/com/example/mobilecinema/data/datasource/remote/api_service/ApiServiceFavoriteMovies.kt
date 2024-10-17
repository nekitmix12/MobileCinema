package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceFavoriteMovies {
    @GET("/favorites")
    suspend fun getFavorites(): MoviesListModel

    @POST("/favorites/{id}/add")
    suspend fun postFavorites(@Path("id") id: String)

    @DELETE("/favorites/{id}/delete")
    suspend fun deleteFavorites(@Path("id") id: String)
}