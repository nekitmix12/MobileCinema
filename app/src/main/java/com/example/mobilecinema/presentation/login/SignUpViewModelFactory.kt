package com.example.mobilecinema.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase

class SignUpViewModelFactory(
    private val loginUserUseCase: RegisterUseCase,
    private val regConverter: RegisterConverter,
    private val addStorageUseCase: AddStorageUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                useCase = loginUserUseCase,
                converter = regConverter,
                addStorageUseCase = addStorageUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}