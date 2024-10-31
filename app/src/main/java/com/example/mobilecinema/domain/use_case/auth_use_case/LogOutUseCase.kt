package com.example.mobilecinema.domain.use_case.auth_use_case

import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogOutUseCase(
    private val authRepository: AuthRepository,
    private val configuration: Configuration
): UseCase<LogOutUseCase.Request,LogOutUseCase.Response>(configuration) {


    override fun process(request: Request): Flow<Response> {
        return flow {
            authRepository.logout()
            emit(Response())
        }
    }

    override fun defaultRequest():Request = Request()

    class Request:UseCase.Request
    class Response:UseCase.Response


}