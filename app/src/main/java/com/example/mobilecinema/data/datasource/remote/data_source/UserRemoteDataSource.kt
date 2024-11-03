package com.example.mobilecinema.data.datasource.remote.data_source

import com.example.mobilecinema.data.model.auth.ProfileDTO
import kotlinx.coroutines.flow.Flow
import com.example.mobilecinema.domain.Result

interface UserRemoteDataSource {
    fun getProfile(): Flow<ProfileDTO>

    suspend fun putProfile(profileDTO: ProfileDTO)
}