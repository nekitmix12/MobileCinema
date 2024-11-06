package com.example.mobilecinema.domain.use_case.friend

import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteAllFriendsUseCase(
    private val friendRepository: FriendRepository,
    configuration: Configuration,
) : UseCase<DeleteAllFriendsUseCase.Request, DeleteAllFriendsUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            emit(
                Response(
                    friendRepository.deleteAll()
                )
            )

        }

    override fun defaultRequest(): Request {
        return Request()
    }

    class Request : UseCase.Request
    data class Response(val answer: Unit) : UseCase.Response

}