package com.example.mobilecinema.domain.use_case.auth_use_case

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegisterUseCase (  configuration: Configuration,
private val authRepository: AuthRepository
) : UseCase<RegisterUseCase.Request,
        RegisterUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        authRepository.registration(request.regData)
            .map {
                Response(it)
            }

    data class Request(val regData: UserRegisterModel) : UseCase.
    Request

    data class Response(val authToken: AuthToken) : UseCase.
    Response
}