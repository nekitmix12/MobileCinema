package com.example.mobilecinema.data.datasource.remote.data_source.inteface

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.LogoutModel
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    fun loginUser(loginCredentials: LoginCredentials):Flow<AuthToken>

    fun registration(userRegisterModel: UserRegisterModel):Flow<AuthToken>

    fun logout(): Flow<LogoutModel>
}