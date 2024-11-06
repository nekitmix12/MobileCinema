package com.example.mobilecinema.data.datasource.local.data_source

import com.example.mobilecinema.data.model.friend.Friend
import kotlinx.coroutines.flow.Flow

interface FriendLocalDataSource {

    suspend fun addFriend(friend: Friend)

    suspend fun deleteFriend(friend: Friend)

    fun getFriends(): Flow<List<Friend>>

    suspend fun deleteAll()
}