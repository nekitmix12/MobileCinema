package com.example.mobilecinema.domain.use_case.favorite_movies_use_case

import com.example.mobilecinema.data.model.favorite_movies.MoviesListModel
import com.example.mobilecinema.data.repository.FavoriteMoviesRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.FavoriteMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteMoviesUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val favoriteMoviesRepository: FavoriteMoviesRepository = FavoriteMoviesRepositoryImpl(),
) : UseCase<GetFavoriteMoviesUseCase.Request, GetFavoriteMoviesUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> {
        return favoriteMoviesRepository.getFavorites().map {
            Response(it)

        }
    }

    override fun defaultRequest(): Request = Request()
    class Request : UseCase.Request
    data class Response(val moviesListModel: MoviesListModel) : UseCase.Response
}