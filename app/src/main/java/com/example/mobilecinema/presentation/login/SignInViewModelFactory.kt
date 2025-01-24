package com.example.mobilecinema.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.domain.converters.auth.AuthConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase

class SignInViewModelFactory(
    private val loginUserUseCase: LoginUserUseCase = LoginUserUseCase(),
    private val authConverter: AuthConverter = AuthConverter(),
    private val addStorageUseCase: AddStorageUseCase = AddStorageUseCase(),
    private val mainContext: MainContext = MainContext.provideInstance()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(
                useCase = loginUserUseCase,
                converter = authConverter,
                addStorageUseCase = addStorageUseCase,
                mainContext = mainContext
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}