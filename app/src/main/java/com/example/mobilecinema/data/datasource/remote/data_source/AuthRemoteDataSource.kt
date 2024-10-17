package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRemoteDataSource {
    suspend fun loginUser(loginCredentials: LoginCredentials):Flow<Response<AuthToken>>

    suspend fun registration(userRegisterModel: UserRegisterModel):Flow<Response<AuthToken>>

    suspend fun logout()
}