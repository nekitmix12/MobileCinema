package com.example.mobilecinema.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilecinema.data.datasource.local.dao.FilmDao
import com.example.mobilecinema.data.datasource.local.dao.FriendDao
import com.example.mobilecinema.data.datasource.local.dao.GenreDao
import com.example.mobilecinema.data.datasource.local.entity.FilmEntity
import com.example.mobilecinema.data.datasource.local.entity.FriendEntity
import com.example.mobilecinema.data.datasource.local.entity.GenreEntity

@Database(
    entities = [
        GenreEntity::class,
        FriendEntity::class,
        FilmEntity::class
    ], version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun genreDao(): GenreDao

    abstract fun friendDao(): FriendDao

    abstract fun filmDao(): FilmDao
}