package com.example.mobilecinema.domain.converters.auth

import android.util.Log
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase

class LogoutConverter {
    fun convert(
        profileResult: Result<LogOutUseCase.Response>
    ): UiState<Unit> {
        return when (profileResult) {
            is Result.Success -> {
                UiState.Success(
                    Unit
                )
            }
            is Result.Error -> {
                Log.d("convert", profileResult.exception)
                UiState.Error(
                    profileResult.exception
                )
            }
        }
    }
}