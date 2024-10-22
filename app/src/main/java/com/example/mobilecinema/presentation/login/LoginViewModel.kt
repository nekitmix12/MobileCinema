package com.example.mobilecinema.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
//import com.example.mobilecinema.data.LoginRepository
import com.example.mobilecinema.data.Result

import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.LoginCredentials
import com.example.mobilecinema.domain.use_case.user_use_case.LoginUserUseCase
import com.example.mobilecinema.presentation.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

//private val loginRepository: LoginRepository
class LoginViewModel(
    private val useCase: LoginUserUseCase,
    private val converter: AuthConverter
) : ViewModel() {


    private val _usersFlow =
        MutableStateFlow<UiState<AuthToken>>(UiState.Loading)

    val usersFlow: StateFlow<UiState<AuthToken>> = _usersFlow


    fun load() {
        viewModelScope.launch {
            useCase.execute(LoginUserUseCase.Request(LoginCredentials("string","string")))
                .map {
                    converter.convert(it)
                }

                .collect {
                    _usersFlow.value = it
                    Log.d("login",it.toString())
                }
        }

    }




/*    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }*/
}