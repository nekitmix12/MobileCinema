package com.example.mobilecinema.data.datasource.remote.data_source.implementation

import com.example.mobilecinema.data.ErrorsConverter
import com.example.mobilecinema.data.ErrorsConverterImpl
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceAuth
import com.example.mobilecinema.data.datasource.remote.data_source.inteface.AuthRemoteDataSource
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.LogoutModel
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class AuthRemoteDataSourceImpl(
    private val apiServiceAuth: ApiServiceAuth = NetworkModule().provideAuthService(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient()
        )
    ),
    private val errorsConverter: ErrorsConverter = ErrorsConverterImpl()
) :
    AuthRemoteDataSource {
    override fun logout(): Flow<LogoutModel> {
        return flow {
            emit(apiServiceAuth.postLogout())
        }.map {
            if (it.body() != null)
                LogoutModel(it.body()!!.token, it.body()!!.message)
            else
                throw IllegalArgumentException(it.message())
        }
    }

    override fun loginUser(loginCredentials: LoginCredentials): Flow<AuthToken> {
        return flow {
            emit(apiServiceAuth.postLogin(loginCredentials))
        }.map {
            if (it.body() != null)
                AuthToken(it.body()!!.token)
            else
                throw IllegalArgumentException(errorsConverter.invokeMethod(it.errorBody(),it.code()))
        }
    }

    override fun registration(userRegisterModel: UserRegisterModel): Flow<AuthToken> {
        return flow {
            emit(apiServiceAuth.postRegister(userRegisterModel))
        }.map {
            if (it.body() != null)
                AuthToken(it.body()!!.token)
            else
                throw IllegalArgumentException(errorsConverter.invokeMethod(it.errorBody(),it.code()))
        }

    }
}