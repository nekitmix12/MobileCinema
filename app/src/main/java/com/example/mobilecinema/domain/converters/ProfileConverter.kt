package com.example.mobilecinema.domain.converters

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
                UiState.Error(
                    profileResult.exception.localizedMessage.orEmpty()
                )
            }
        }
    }
}