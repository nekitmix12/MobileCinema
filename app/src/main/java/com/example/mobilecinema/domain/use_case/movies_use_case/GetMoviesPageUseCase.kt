package com.example.mobilecinema.domain.use_case.movies_use_case

import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.data.repository.MoviesRepositoryImpl
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMoviesPageUseCase(
    configuration: Configuration = Configuration(Dispatchers.IO),
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl()
): UseCase<GetMoviesPageUseCase.Request,
        GetMoviesPageUseCase.Response>(configuration) {

    override fun process(request: Request): Flow<Response> =
        moviesRepository.getMoviesPage(request.pageData)
            .map {
                Response(it)
            }

    data class Request(val pageData: Int) : UseCase.
    Request

    data class Response(val moviesPagedListModel: MoviesPagedListModel) : UseCase.
    Response
}