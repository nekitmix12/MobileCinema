package com.example.mobilecinema.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dislike_film")
data class FilmEntity (
    @PrimaryKey @ColumnInfo(name = "id") val id: String
)