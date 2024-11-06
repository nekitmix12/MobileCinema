package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.friend.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {

    suspend fun addFriend(friend: Friend)

    suspend fun deleteFriend(friend: Friend)

    fun getFriends(): Flow<List<Friend>>

    suspend fun deleteAll()
}