package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.auth.ProfileDTO
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(): Flow<ProfileDTO>

    suspend fun putProfile(profileDTO: ProfileDTO)
}