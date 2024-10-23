package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.LogoutModel
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import kotlinx.coroutines.flow.Flow


interface AuthRepository {
    fun loginUser(loginCredentials: LoginCredentials): Flow<AuthToken>

    fun registration(registerModel: UserRegisterModel): Flow<AuthToken>

    fun logout(): Flow<LogoutModel>

    fun save(token: String)

    fun getToken():String?

    fun clearToken()
}