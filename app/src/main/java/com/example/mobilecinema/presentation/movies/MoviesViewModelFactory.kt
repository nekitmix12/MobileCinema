package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.presentation.feed.FeedViewModel
import com.example.mobilecinema.presentation.feed.MoviesConverter

class MoviesViewModelFactory(
    private val loginUserUseCase: GetMoviesPageUseCase,
    private val authConverter: MoviesConverter,
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(
                useCase = loginUserUseCase,
                converter = authConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}