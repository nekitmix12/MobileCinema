package com.example.mobilecinema.domain.use_case.friend

import com.example.mobilecinema.data.model.friend.Friend
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddFriendUseCase(
    private val friendRepository: FriendRepository,
    configuration: Configuration,
) : UseCase<AddFriendUseCase.Request, AddFriendUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        flow {
            emit(
                Response(
                    friendRepository.addFriend(request.friend)
                )
            )

        }

    data class Request(val friend: Friend) : UseCase.Request
    data class Response(val answer: Unit) : UseCase.Response

}