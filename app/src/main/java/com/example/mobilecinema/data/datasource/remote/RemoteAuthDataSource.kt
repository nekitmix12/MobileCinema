package com.example.mobilecinema.data.datasource.remote

import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import retrofit2.http.POST

interface RemoteAuthDataSource {
    @POST("/account/register")
    suspend fun postRegister(): UserRegisterModel

    @POST("/account/login")
    suspend fun postLogin(): LoginCredentials

    @POST("/account/logout")
    suspend fun postLogout()
}