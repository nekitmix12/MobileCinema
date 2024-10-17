package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.auth.ProfileModel
import retrofit2.http.GET
import retrofit2.http.PUT

interface ApiServiceUser {
    @GET("/account/profile")
    suspend fun getProfile(): ProfileModel

    @PUT("/account/profile")
    suspend fun editProfile(profileModel: ProfileModel)
}