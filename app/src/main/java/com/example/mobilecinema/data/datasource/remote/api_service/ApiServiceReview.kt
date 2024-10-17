package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceReview {
    @POST("/movie/{movieId}/review/add")
    suspend fun addReview(@Path("movieId") movieId: String, movieDetailsModel: MovieDetailsModel)

    @PUT("/movie/{movieId}/review/{id}/edit")
    suspend fun editReview(
        @Path("movieId") movieId: String,
        @Path("id") id: String,
        movieDetailsModel: MovieDetailsModel
    )

    @DELETE("/movie/{movieId}/review/{id}/delete")
    suspend fun deleteReview(@Path("movieId") movieId: String, @Path("id") id: String)
}