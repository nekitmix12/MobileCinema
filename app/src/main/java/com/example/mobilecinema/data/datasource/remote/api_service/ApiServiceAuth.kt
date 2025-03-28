package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.LogoutModel
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiServiceAuth {
    @POST("/api/account/register")
    suspend fun postRegister(@Body userRegisterModel:UserRegisterModel): Response<AuthToken>

    @POST("/api/account/login")
    suspend fun postLogin(@Body loginCredentials:LoginCredentials): Response<AuthToken>

    @POST("/api/account/logout")
    suspend fun postLogout():Response<LogoutModel>
}