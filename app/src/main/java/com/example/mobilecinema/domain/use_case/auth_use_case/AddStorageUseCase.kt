package com.example.mobilecinema.domain.use_case.auth_use_case

import android.util.Log
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddStorageUseCase(
    private val authRepository: AuthRepository
) {
    fun addStorage(value: String){
        authRepository.save(value)
        Log.d("add_storage_use_case",value)
    }

}