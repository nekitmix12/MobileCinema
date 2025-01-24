package com.example.mobilecinema.data.datasource.local.data_source

import androidx.room.Room
import com.example.mobilecinema.data.datasource.local.AppDataBase
import com.example.mobilecinema.data.datasource.local.dao.FilmDao
import com.example.mobilecinema.data.datasource.local.dao.FilmDao_Impl
import com.example.mobilecinema.data.datasource.local.entity.FilmEntity
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import com.example.mobilecinema.di.MainContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilmLocalDataSourceImpl(
    private val filmDao: FilmDao = FilmDao_Impl(
        Room.databaseBuilder(
            MainContext.provideInstance().provideContext(),
            AppDataBase::class.java, "app_database"
        ).build()
    ),
) : FilmLocalDataSource {
    override suspend fun addFilm(filmModel: ShortMovieModel) {
        filmDao.insertFilm(
            FilmEntity(
                id = filmModel.id
            )
        )
    }

    override suspend fun deleteFilm(filmModel: ShortMovieModel) {
        filmDao.deleteFilm(
            FilmEntity(
                id = filmModel.id,
            )
        )
    }

    override fun getFilm(): Flow<List<ShortMovieModel>> {
        return filmDao.getFilm().map {
            it.map { el ->
                ShortMovieModel(
                    id = el.id,
                )
            }
        }
    }

    override suspend fun deleteAll() {
        filmDao.deleteAll()
    }
}