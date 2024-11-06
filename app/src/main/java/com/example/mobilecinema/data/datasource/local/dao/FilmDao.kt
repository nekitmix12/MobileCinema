package com.example.mobilecinema.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilecinema.data.datasource.local.entity.FilmEntity
import com.example.mobilecinema.data.datasource.local.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {
    @Query("SELECT * FROM dislike_film")
    fun getFilm(): Flow<List<FilmEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFilm(film: FilmEntity)

    @Delete
    fun deleteFilm(film: FilmEntity)

    @Query("Delete From dislike_film")
    fun deleteAll()
}