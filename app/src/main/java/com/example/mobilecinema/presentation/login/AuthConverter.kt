package com.example.mobilecinema.presentation.login

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import com.example.mobilecinema.domain.Result

class AuthConverter {
    fun convert(postListResult: Result
    <LoginUserUseCase.
    Response>): UiState<AuthToken> {
        return when(postListResult) {
            is Result.Error -> {
                UiState.Error(postListResult.
                exception.localizedMessage.orEmpty())
            }
            is Result.Success -> {
                UiState.Success(AuthToken(postListResult.data.authToken.token))
            }
        }
    }
}