package com.example.mobilecinema.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase

class LoginViewModelFactory(
    private val loginUserUseCase: LoginUserUseCase,
    private val authConverter: AuthConverter,
    private val addStorageUseCase: AddStorageUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                useCase = loginUserUseCase,
                converter = authConverter,
                addStorageUseCase = addStorageUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}