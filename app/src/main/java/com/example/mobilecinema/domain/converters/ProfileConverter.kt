package com.example.mobilecinema.domain.converters

import android.util.Log
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase

class ProfileConverter {
    fun convert(
        profileResult: Result<GetProfileUseCase.Response>
    ): UiState<ProfileDTO> {
        return when (profileResult) {
            is Result.Success -> {
                UiState.Success(
                    profileResult.data.profileDTO
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