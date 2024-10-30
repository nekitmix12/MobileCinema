package com.example.mobilecinema.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.MoviesConverter
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase


class FeedViewModelFactory(private val loginUserUseCase: GetMoviesPageUseCase,
                           private val authConverter: MoviesConverter,

                           ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(
                useCase = loginUserUseCase,
                converter = authConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}