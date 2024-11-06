package com.example.mobilecinema.data.repository

import com.example.mobilecinema.data.datasource.local.data_source.FriendLocalDataSource
import com.example.mobilecinema.data.model.friend.Friend
import com.example.mobilecinema.domain.repository.FriendRepository
import kotlinx.coroutines.flow.Flow

class FriendRepositoryImpl(
    private val friendLocalDataSource: FriendLocalDataSource
): FriendRepository {
    override suspend fun addFriend(friend: Friend) {
        friendLocalDataSource.addFriend(friend)
    }

    override suspend fun deleteFriend(friend: Friend) {
        friendLocalDataSource.deleteFriend(friend)
    }

    override fun getFriends(): Flow<List<Friend>> {
        return friendLocalDataSource.getFriends()
    }

    override suspend fun deleteAll() {
       friendLocalDataSource.deleteAll()
    }
}