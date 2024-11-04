package com.example.mobilecinema.domain.use_case.user_use_case

import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PutProfileUseCase(
    private val userRepository: UserRepository,
    configuration: Configuration,
) : UseCase<PutProfileUseCase.Request, PutProfileUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> = flow {
        emit(Response((userRepository.putProfile(request.userModel))))
    }


    data class Request(val userModel: ProfileDTO) : UseCase.Request
    class Response(val putProfile: Unit) : UseCase.Response
}