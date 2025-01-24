package com.example.mobilecinema.domain.use_case.favorite_movies_use_case

import com.example.mobilecinema.data.repository.FavoriteMoviesRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.FavoriteMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddFavoriteMovieUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val favoriteMoviesRepository: FavoriteMoviesRepository = FavoriteMoviesRepositoryImpl(),
) : UseCase<AddFavoriteMovieUseCase.Request, AddFavoriteMovieUseCase.Response>(configuration) {
    override fun process(request: Request): Flow<Response> {
        return flow {
            emit(
                Response(
                    favoriteMoviesRepository.postFavorites(request.id)
                )
            )
        }
    }

    data class Request(val id: String) : UseCase.Request
    data class Response(val answer: Unit) : UseCase.Response
}