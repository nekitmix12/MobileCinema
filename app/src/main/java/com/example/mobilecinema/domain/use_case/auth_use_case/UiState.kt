package com.example.mobilecinema.domain.use_case.auth_use_case

sealed class UiState<out T : Any> {
    data object Loading : UiState<Nothing>()
    data class Error<out T : Any>(val errorMessage: String) : UiState<T>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
}