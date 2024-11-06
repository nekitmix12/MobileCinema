package com.example.mobilecinema.data.datasource.local.data_source

import com.example.mobilecinema.data.datasource.local.dao.GenreDao
import com.example.mobilecinema.data.datasource.local.entity.GenreEntity
import com.example.mobilecinema.data.model.movie.GenreModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GenreLocalDataSourceImpl(
    private val genreDao: GenreDao
):GenreLocalDataSource{
    override suspend fun addGenres(genreModel: GenreModel) {
        genreDao.insertGenre(GenreEntity(
            id = genreModel.id,
            genre = genreModel.genreName ?: ""
        ))
    }

    override suspend fun deleteGenre(genreModel: GenreModel) {
        genreDao.deleteGenre(GenreEntity(
            id = genreModel.id,
            genre = genreModel.genreName ?: ""
        ))
    }

    override fun getGenres(): Flow<List<GenreModel>> {
        return genreDao.getGenres().map{
            it.map {it1 ->
                GenreModel(
                    id = it1.id,
                    genreName = it1.genre
                )
            }
        }
    }

    override suspend fun deleteAll() {
        genreDao.deleteAll()
    }
}