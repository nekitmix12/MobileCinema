package com.example.mobilecinema.data.datasource.local.data_source

import com.example.mobilecinema.data.datasource.local.dao.FilmDao
import com.example.mobilecinema.data.datasource.local.entity.FilmEntity
import com.example.mobilecinema.data.model.movie.ShortMovieModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilmLocalDataSourceImpl(
    private val filmDao: FilmDao,
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