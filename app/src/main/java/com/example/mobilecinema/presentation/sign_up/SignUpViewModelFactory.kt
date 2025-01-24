package com.example.mobilecinema.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import com.example.mobilecinema.domain.converters.auth.RegisterConverter

class SignUpViewModelFactory(
    private val registerUserUseCase: RegisterUseCase = RegisterUseCase(),
    private val regConverter: RegisterConverter = RegisterConverter(),
    private val addStorageUseCase: AddStorageUseCase = AddStorageUseCase()
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(
                registerUseCase = registerUserUseCase,
                registerConverter = regConverter,
                addStorageUseCase = addStorageUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}