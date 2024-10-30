package com.example.mobilecinema.domain.repository

import com.example.mobilecinema.data.model.movie.MovieElementModel

//создал с тем что в будущем понадобится как то еще обрабатывать данные
interface FilmInfoRepository {
    fun getFilmRating(movies: List<MovieElementModel>): List<Float>
}