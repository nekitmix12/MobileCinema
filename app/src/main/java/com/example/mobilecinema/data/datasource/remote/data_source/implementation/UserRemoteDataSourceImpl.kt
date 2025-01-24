package com.example.mobilecinema.data.datasource.remote.data_source.implementation

import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceUser
import com.example.mobilecinema.data.datasource.remote.data_source.inteface.UserRemoteDataSource
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class UserRemoteDataSourceImpl(
    private val apiServiceUser: ApiServiceUser = NetworkModule().provideUserService(
        NetworkModule().provideRetrofit(
            NetworkModule().provideOkHttpClient(
                AuthInterceptor()
            )
        )
    ),
) : UserRemoteDataSource {
    override fun getProfile(): Flow<ProfileDTO> {
        return flow {
            emit(apiServiceUser.getProfile())
        }.map {
            ProfileDTO(
                id = it.id,
                nickName = it.nickName,
                email = it.email,
                avatarLink = it.avatarLink,
                name = it.name,
                birthDate = it.birthDate,
                gender = it.gender
            )
        }
    }

    override suspend fun putProfile(profileDTO: ProfileDTO) {
        apiServiceUser.editProfile(profileDTO)
    }
}