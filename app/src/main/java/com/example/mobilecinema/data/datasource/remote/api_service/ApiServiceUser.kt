package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.auth.ProfileDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ApiServiceUser {
    @GET("api/account/profile")
    suspend fun getProfile(): ProfileDTO

    @PUT("api/account/profile")
    suspend fun editProfile(@Body profileDTO: ProfileDTO)
}