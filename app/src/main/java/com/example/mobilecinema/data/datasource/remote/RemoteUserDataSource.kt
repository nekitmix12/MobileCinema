package com.example.mobilecinema.data.datasource.remote

import com.example.mobilecinema.data.model.ProfileModel
import retrofit2.http.GET
import retrofit2.http.PUT

interface RemoteUserDataSource {
    @GET("/account/profile")
    suspend fun getProfile(): ProfileModel

    @PUT("/account/profile")
    suspend fun editProfile(profileModel: ProfileModel)
}