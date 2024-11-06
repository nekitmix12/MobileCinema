package com.example.mobilecinema.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilecinema.data.datasource.local.entity.GenreEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface GenreDao {

    @Query("SELECT * FROM favorite_genre")
    fun getGenres(): Flow<List<GenreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genre: GenreEntity)

    @Delete
    suspend fun deleteGenre(genre: GenreEntity)

    @Query("Delete From favorite_genre")
    suspend fun deleteAll()
}