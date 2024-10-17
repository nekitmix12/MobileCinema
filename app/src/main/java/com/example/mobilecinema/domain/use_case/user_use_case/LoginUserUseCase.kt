package com.example.mobilecinema.domain.use_case.user_use_case

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.domain.repository.UserRepository
import kotlinx.coroutines.flow.map
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.UseCaseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response


class LoginUserUseCase(private val userRepository: UserRepository):UseCase<LoginCredentials,Response<AuthToken>>(Dispatchers.IO) {
     fun loginUser(loginCredentials: LoginCredentials) =
        userRepository.loginUser(loginCredentials).map { it ->
            Result.Success(it) as Result<Response<AuthToken>>
        }
            .flowOn(Dispatchers.IO)
            .catch {
                emit(Result.Error(UseCaseException.extractException(it)))
            }

    override fun executeData(input: LoginCredentials): Flow<Response<AuthToken>> {
        return userRepository.loginUser(input)
    }
}