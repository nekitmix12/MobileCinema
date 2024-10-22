package com.example.mobilecinema.domain.use_case.user_use_case

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegistrationUserUseCase (
    configuration: Configuration,
private val userRepository: UserRepository
) : UseCase<RegistrationUserUseCase.Request,
        RegistrationUserUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        userRepository.registration(request.registerData)
            .map {
                Response()
            }

    data class Request(val registerData: UserRegisterModel) : UseCase.
    Request

    data class Response() : UseCase.
    Response
}