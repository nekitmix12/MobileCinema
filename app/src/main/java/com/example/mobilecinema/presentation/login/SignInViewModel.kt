package com.example.mobilecinema.presentation.login

//import com.example.mobilecinema.data.LoginRepository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.UserInfo
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.domain.converters.auth.AuthConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

//private val loginRepository: LoginRepository
class SignInViewModel(
    private val addStorageUseCase: AddStorageUseCase,
    private val useCase: LoginUserUseCase,
    private val converter: AuthConverter,
    private val mainContext: MainContext,
) : ViewModel() {

    private val _usersFlow =
        MutableStateFlow<UiState<AuthToken>>(UiState.Loading)

    val usersFlow: StateFlow<UiState<AuthToken>> = _usersFlow

    var isConfirmed =
        MutableStateFlow(false)

    private val _userLogin =
        MutableStateFlow("")

    val userLogin: StateFlow<String> = _userLogin

    private val _userPassword =
        MutableStateFlow("")

    val userPassword: StateFlow<String> = _userPassword

    private val _error = MutableStateFlow<String?>("")
    val error: StateFlow<String?> = _error

    fun load() {
        if (_error.value != null) return
        val login = _userLogin.value
        val password = _userPassword.value
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
            usersFlow.collect {
                if (it is UiState.Error) {
                    _error.value = it.errorMessage
                }

                if (it is UiState.Success) {
                    Log.d("token is ", it.data.token)
                    isConfirmed.value = true
                    UserInfo.login = login
                    UserInfo.password = password
                    addStorageUseCase.addStorage(it.data.token)
                }
            }
        }
    }

    fun checkErrors() {
        var tempString = ""
        if (userLogin.value.isEmpty())
            tempString += mainContext.provideContext().getString(R.string.login_error_null)
        if (tempString != "")
            tempString += " ${mainContext.provideContext().getString(R.string.and)} "
        if (userPassword.value.isEmpty())
            tempString += mainContext.provideContext().getString(R.string.password_error_null)

        _error.value = if (tempString == "")
            null
        else
            tempString + ""
    }

    fun setLogin(login: String) {
        _userLogin.value = login
    }

    fun setPassword(password: String) {
        _userPassword.value = password
    }
}