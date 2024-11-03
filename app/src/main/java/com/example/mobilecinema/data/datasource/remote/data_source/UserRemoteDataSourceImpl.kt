package com.example.mobilecinema.data.datasource.remote.data_source

import android.util.Log
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceUser
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.UseCaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import com.example.mobilecinema.domain.Result


class UserRemoteDataSourceImpl(
    private val apiServiceUser: ApiServiceUser,
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

    override suspend fun putProfile(profileDTO: ProfileDTO){
        apiServiceUser.editProfile(profileDTO)
    }
}