package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSource
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.LogoutModel
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.repository.AuthRepository
import com.example.mobilecinema.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authRemoteDataSource:
    AuthRemoteDataSource,
    private val localStorageRepository:
    LocalStorageRepository
) : AuthRepository {

    override fun loginUser(loginCredentials: LoginCredentials): Flow<AuthToken> =
        authRemoteDataSource.loginUser(loginCredentials)


    override fun registration(registerModel: UserRegisterModel): Flow<AuthToken> =
        authRemoteDataSource.registration(registerModel)


    override fun logout(): Flow<LogoutModel> =
        authRemoteDataSource.logout()

    override fun save(token: String) {
        localStorageRepository.save(token)
    }

    override fun getToken(): String? =
        localStorageRepository.getToken()


    override fun clearToken() {
        localStorageRepository.clearToken()
    }


}