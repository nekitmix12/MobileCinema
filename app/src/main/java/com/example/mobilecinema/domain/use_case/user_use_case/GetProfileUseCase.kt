package com.example.mobilecinema.domain.use_case.user_use_case

import android.util.Log
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.data.repository.UserRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProfileUseCase(
    private val userRepository: UserRepository = UserRepositoryImpl(),
    configuration: Configuration = UseCase.Configuration(Dispatchers.IO)
) : UseCase<GetProfileUseCase.Request, GetProfileUseCase.Response>(configuration) {

    data class Response(val profileDTO: ProfileDTO) : UseCase.Response
    class Request : UseCase.Request

    override fun defaultRequest(): Request = Request()

    override fun process(request: Request): Flow<Response> {
        return userRepository.getProfile().map {
            Response(it)
        }
    }
}