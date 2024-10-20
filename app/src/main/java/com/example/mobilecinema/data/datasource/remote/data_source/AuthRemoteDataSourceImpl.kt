package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceAuth
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class AuthRemoteDataSourceImpl (private val apiServiceAuth: ApiServiceAuth) :
    AuthRemoteDataSource {
    override fun logout() {
        apiServiceAuth.postLogout()
    }

    override fun loginUser(loginCredentials: LoginCredentials): Flow<AuthToken> {
        return flow {
                emit(apiServiceAuth.postLogin(loginCredentials))
            }.map {
                AuthToken(it.token)
        }
    }

    override fun registration(userRegisterModel: UserRegisterModel):Flow<AuthToken> {
        return flow {
            emit(apiServiceAuth.postRegister(userRegisterModel))
        }.map {
            AuthToken(it.token)
        }

    }
}