package com.example.mobilecinema.data

import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSource
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.repository.UserRepository
import retrofit2.Response

class UserRepositoryImpl(
    private val authRemoteDataSource:
    AuthRemoteDataSource
) : UserRepository {

    override fun loginUser(loginCredentials: LoginCredentials): Response<AuthToken> =
        authRemoteDataSource.loginUser(loginCredentials)


    override fun registration(registerModel: UserRegisterModel): Response<AuthToken> =
        authRemoteDataSource.registration(registerModel)


    override fun logout() {
        authRemoteDataSource.logout()
    }

}