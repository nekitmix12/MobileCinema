package com.example.mobilecinema.data.datasource.remote.data_source

import android.location.Location.convert
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceAuth
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.UseCaseException
import com.example.mobilecinema.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class AuthRemoteDataSourceImpl (private val apiServiceAuth: ApiServiceAuth) :
    AuthRemoteDataSource {
    override suspend fun logout() {
        apiServiceAuth.postLogout()
    }

    override suspend fun loginUser(loginCredentials: LoginCredentials): Flow<Response<AuthToken>> {
        val response = apiServiceAuth.postLogin(loginCredentials)

        if (response.isSuccessful) {
            return flow {
                emit(apiServiceAuth.postLogin(loginCredentials))
            }.catch {
                throw UseCaseException.UserException(it)
            }
        } else {
            throw Exception("Failed to get token")
        }
    }

    override suspend fun registration(registerModel: UserRegisterModel):Flow<Response<AuthToken>> {
        val response = apiServiceAuth.postRegister(registerModel)

        if (response.isSuccessful) {
            return flow {
                emit(apiServiceAuth.postRegister(registerModel))
            }.catch {
                throw UseCaseException.UserException(it)
            }
        } else {
            throw Exception("Failed to get token")
        }
    }
}