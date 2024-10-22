package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRemoteDataSource {
    fun loginUser(loginCredentials: LoginCredentials):Flow<AuthToken>

    fun registration(userRegisterModel: UserRegisterModel):Flow<AuthToken>

    suspend fun logout()
}