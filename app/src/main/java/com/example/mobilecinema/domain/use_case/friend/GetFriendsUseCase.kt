package com.example.mobilecinema.domain.use_case.friend

import com.example.mobilecinema.data.model.friend.Friend
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetFriendsUseCase(
    private val friendRepository: FriendRepository,
    configuration: Configuration,
) : UseCase<GetFriendsUseCase.Request, GetFriendsUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        friendRepository.getFriends()
            .map {
                Response(it)
            }


    override fun defaultRequest(): Request {
        return Request()
    }

    class Request : UseCase.Request
    data class Response(val answer: List<Friend>) : UseCase.Response

}