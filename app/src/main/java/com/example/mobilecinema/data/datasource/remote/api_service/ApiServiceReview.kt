package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.movie.MovieDetailsModel
import com.example.mobilecinema.data.model.review.ReviewModifyModel
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiServiceReview {
    @POST("/api/movie/{movieId}/review/add")
    fun addReview(
        @Path("movieId") movieId: String,
        @Body reviewModifyModel: ReviewModifyModel
    )

    @PUT("/api/movie/{movieId}/review/{id}/edit")
    fun editReview(
        @Path("movieId") movieId: String,
        @Path("id") id: String,
        @Body reviewModifyModel: ReviewModifyModel
    )

    @DELETE("/api/movie/{movieId}/review/{id}/delete")
    fun deleteReview(@Path("movieId") movieId: String, @Path("id") id: String)
}