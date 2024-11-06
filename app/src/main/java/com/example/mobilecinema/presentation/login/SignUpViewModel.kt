package com.example.mobilecinema.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.DataConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import com.example.mobilecinema.domain.use_case.validate.ValidateBirthDate
import com.example.mobilecinema.domain.use_case.validate.ValidateConfirmPassword
import com.example.mobilecinema.domain.use_case.validate.ValidateEmail
import com.example.mobilecinema.domain.use_case.validate.ValidateGender
import com.example.mobilecinema.domain.use_case.validate.ValidateLogin
import com.example.mobilecinema.domain.use_case.validate.ValidateName
import com.example.mobilecinema.domain.use_case.validate.ValidatePassword
import com.example.mobilecinema.domain.model.RegisterModel
import com.example.mobilecinema.domain.use_case.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class SignUpViewModel(
    private val validateLogin: ValidateLogin = ValidateLogin(),
    private val validateName: ValidateName = ValidateName(),
    private val validateGender: ValidateGender = ValidateGender(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateConfirmPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
    private val validateBirthDate: ValidateBirthDate = ValidateBirthDate(),
    private val dataConverter: DataConverter = DataConverter(),
    private val addStorageUseCase: AddStorageUseCase,
    private val useCase: RegisterUseCase,
    private val converter: RegisterConverter
) : ViewModel() {
    private val _usersFlow = MutableStateFlow<UiState<AuthToken>>(UiState.Loading)

    val usersFlow: StateFlow<UiState<AuthToken>> = _usersFlow

    var state by mutableStateOf(RegisterModel())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is RegistrationFormEvent.LoginChanged -> {
                state = state.copy(login = event.login)

            }

            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)

            }

            is RegistrationFormEvent.ConfirmPasswordChanged -> {
                state = state.copy(confirmPassword = event.confirmPassword)

            }

            is RegistrationFormEvent.BirthDateChanged -> {
                state = state.copy(
                    birthDate = if (event.birthDate.length > 10) dataConverter.convertDateFormat(
                        event.birthDate
                    ) else event.birthDate
                )

            }

            is RegistrationFormEvent.GenderChanged -> {
                state = state.copy(gender = event.gender)

            }

            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun load() {
        Log.d("register",RegisterUseCase.Request(
            UserRegisterModel(
                userName = state.name,
                userLogin = state.login,
                password = state.password,
                birthDate = state.birthDate,
                gender = state.gender
            )
        ).toString())

        viewModelScope.launch {
            useCase.execute(
                RegisterUseCase.Request(
                    UserRegisterModel(
                        userName = state.name,
                        userLogin = state.login,
                        password = state.password,
                        birthDate = state.birthDate,
                        gender = state.gender
                    )
                )
            ).map {
                    converter.convert(it)
                }

                .collect {
                    _usersFlow.value = it

                }

        }
        viewModelScope.launch {
            usersFlow.collect {
                when (it) {
                    is UiState.Loading -> {

                    }

                    is UiState.Error -> {
                        Log.d("register", it.errorMessage)
                    }

                    is UiState.Success -> {
                        Log.d("register", it.data.token)
                        addStorageUseCase.addStorage(it.data.token)
                    }
                }
            }
        }

    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val confirmPasswordResult =
            validateConfirmPassword.execute(state.confirmPassword, state.confirmPassword)
        val nameResult = validateName.execute(state.name)
        val loginResult = validateLogin.execute(state.login)
        val birthDateResult = validateBirthDate.execute(state.birthDate)
        val genderResult = validateGender.execute(state.gender)

        val hasError = listOf(
            nameResult, emailResult, passwordResult, confirmPasswordResult
        ).any { it.errorManager != null }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorManager,
                emailError = emailResult.errorManager,
                loginError = loginResult.errorManager,
                passwordError = passwordResult.errorManager,
                confirmPasswordError = confirmPasswordResult.errorManager,
                birthDateError = birthDateResult.errorManager,
                genderError = genderResult.errorManager
            )
            Log.d("use_case", state.toString())
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
        load()
    }


    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}