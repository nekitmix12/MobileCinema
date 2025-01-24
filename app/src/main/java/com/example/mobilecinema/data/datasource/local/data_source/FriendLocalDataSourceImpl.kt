package com.example.mobilecinema.data.datasource.local.data_source

import androidx.room.Room
import com.example.mobilecinema.data.datasource.local.AppDataBase
import com.example.mobilecinema.data.datasource.local.dao.FriendDao
import com.example.mobilecinema.data.datasource.local.dao.FriendDao_Impl
import com.example.mobilecinema.data.datasource.local.entity.FriendEntity
import com.example.mobilecinema.data.model.friend.Friend
import com.example.mobilecinema.di.MainContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FriendLocalDataSourceImpl(
    private val friendDao: FriendDao = FriendDao_Impl(
        Room.databaseBuilder(
            MainContext.provideInstance().provideContext(),
            AppDataBase::class.java, "app_database"
        ).build()
    ),
) : FriendLocalDataSource {
    override suspend fun addFriend(friend: Friend) {
        friendDao.insertFriend(
            FriendEntity(
                id = friend.id,
                name = friend.name,
                url = friend.url
            )
        )
    }

    override suspend fun deleteFriend(friend: Friend) {
        friendDao.deleteFriend(
            FriendEntity(
                id = friend.id,
                name = friend.name,
                url = friend.url
            )
        )
    }

    override fun getFriends(): Flow<List<Friend>> {
        return friendDao.getFriends().map {
            it.map { el ->
                Friend(
                    id = el.id,
                    name = el.name,
                    url = el.url
                )
            }

        }
    }


    override suspend fun deleteAll() {
        friendDao.deleteAllFriends()
    }
}