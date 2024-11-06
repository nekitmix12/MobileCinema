package com.example.mobilecinema.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilecinema.data.datasource.local.entity.FilmEntity
import com.example.mobilecinema.data.datasource.local.entity.FriendEntity
import com.example.mobilecinema.data.datasource.local.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {
    @Query("SELECT * FROM friend")
    fun getFriends(): Flow<List<FriendEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFriend(friend: FriendEntity)

    @Delete
    fun deleteFriend(friend: FriendEntity)

    @Query("DELETE FROM friend")
    fun deleteAllFriends()
}