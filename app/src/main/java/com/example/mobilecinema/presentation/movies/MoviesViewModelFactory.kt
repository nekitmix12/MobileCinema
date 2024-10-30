package com.example.mobilecinema.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.FavoriteMoviesConverter
import com.example.mobilecinema.domain.use_case.favorite_movies_use_case.GetFavoriteMoviesUseCase
import com.example.mobilecinema.domain.use_case.movies_use_case.GetMoviesPageUseCase
import com.example.mobilecinema.domain.converters.MoviesConverter

class MoviesViewModelFactory(
    private val loginUserUseCase: GetMoviesPageUseCase,
    private val authConverter: MoviesConverter,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val favoriteMoviesConverter: FavoriteMoviesConverter
) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(
                useCase = loginUserUseCase,
                converter = authConverter,
                getFavoriteMoviesUseCase = getFavoriteMoviesUseCase,
                favoriteMoviesConverter = favoriteMoviesConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}