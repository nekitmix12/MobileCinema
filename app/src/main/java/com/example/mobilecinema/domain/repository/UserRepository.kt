package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface UserRepository {
    fun loginUser(loginCredentials: LoginCredentials): Flow<AuthToken>

    fun registration(registerModel: UserRegisterModel): Flow<AuthToken>

    fun logout()
}