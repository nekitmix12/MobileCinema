package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.presentation.feed.MoviesConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val useCase: GetMoviesPageUseCase,
    private val converter: MoviesConverter
) : ViewModel() {
    private val _movies = MutableStateFlow<UiState<MoviesPagedListModel>>(UiState.Loading)
    val movies: StateFlow<UiState<MoviesPagedListModel>> = _movies
    private var id = 1

    fun load(){
        viewModelScope.launch {
            useCase.execute(
                GetMoviesPageUseCase.Request(
                    pageData = id
                )
            )
                .map {
                    converter.convert(it)
                }

                .collect {
                    _movies.value = it

                }

        }
    }
    fun incId(){
        id += 1
    }
    fun setCommonId(){
        id = 1
    }
}