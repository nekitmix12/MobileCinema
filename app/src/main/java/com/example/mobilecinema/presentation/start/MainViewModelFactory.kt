package com.example.mobilecinema.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.presentation.movies.MoviesViewModel

class MainViewModelFactory(
    private val getProfileUseCase: GetProfileUseCase,
    private val profileConverter: ProfileConverter
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                getProfileUseCase = getProfileUseCase,
                profileConverter = profileConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}