package com.example.mobilecinema.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.mobilecinema.data.LoginRepository

import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//private val loginRepository: LoginRepository
class LoginViewModel(
    private val addStorageUseCase: AddStorageUseCase,
    private val useCase: LoginUserUseCase,
    private val converter: AuthConverter
) : ViewModel() {

    private val _usersFlow =
        MutableStateFlow<UiState<AuthToken>>(UiState.Loading)

    val usersFlow: StateFlow<UiState<AuthToken>> = _usersFlow

    private val _userLogin =
        MutableStateFlow<String>("")

    val userLogin: StateFlow<String> = _userLogin

    private val _userPassword =
        MutableStateFlow<String>("")

    val userPassword: StateFlow<String> = _userPassword

    private fun validateInput(): Boolean
    = userPassword.value == "" && userLogin.value == ""


    fun load() {
        if (validateInput())return
        viewModelScope.launch {
            useCase.execute(
                LoginUserUseCase.Request(
                    LoginCredentials(
                        userLogin.value,
                        userPassword.value
                    )
                )
            )
                .map {
                    converter.convert(it)
                }

                .collect {
                    _usersFlow.value = it

                }

        }

        viewModelScope.launch {
            usersFlow.collect{
                when(it){
                    is UiState.Loading->{

                    }
                    is UiState.Error -> {
                        Log.d("login",it.errorMessage)
                    }
                    is UiState.Success -> {
                        Log.d("login",it.data.token)
                        addStorageUseCase.addStorage(it.data.token)
                    }
                }
            }
        }
    }

    fun setLogin(login: String) {
        _userLogin.value = login
    }

    fun setPassword(password: String) {
        _userPassword.value = password
    }


}