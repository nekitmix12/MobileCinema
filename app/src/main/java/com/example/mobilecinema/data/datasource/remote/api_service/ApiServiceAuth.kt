package com.example.mobilecinema.data.datasource.remote.api_service

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import retrofit2.Response
import retrofit2.http.POST

interface ApiServiceAuth {
    @POST("/account/register")
    suspend fun postRegister(userRegisterModel:UserRegisterModel): Response<AuthToken>

    @POST("/account/login")
    suspend fun postLogin(loginCredentials:LoginCredentials): Response<AuthToken>

    @POST("/account/logout")
    suspend fun postLogout()
}