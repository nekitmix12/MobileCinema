package com.example.mobilecinema.domain.converters.auth

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import com.example.mobilecinema.domain.use_case.UiState

class RegisterConverter {
    fun convert(
        postListResult: Result<RegisterUseCase.
        Response>
    ): UiState<AuthToken> {
        return when (postListResult) {
            is Result.Error -> {
                UiState.Error(
                    postListResult.exception
                )
            }

            is Result.Success -> {
                UiState.Success(AuthToken(postListResult.data.authToken.token))
            }
        }
    }
}