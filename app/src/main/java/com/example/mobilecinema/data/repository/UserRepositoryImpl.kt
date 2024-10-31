package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.remote.data_source.UserRemoteDataSource
import com.example.mobilecinema.data.model.auth.ProfileDTO
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRemoteDataSource {
    override fun getProfile(): Flow<ProfileDTO> {
        return userRemoteDataSource.getProfile()
    }

    override suspend fun putProfile(profileDTO: ProfileDTO) {
        userRemoteDataSource.putProfile(profileDTO)
    }


}