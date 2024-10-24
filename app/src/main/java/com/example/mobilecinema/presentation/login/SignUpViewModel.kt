package com.example.mobilecinema.presentation.login

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.ValidateBirthDate
import com.example.mobilecinema.domain.use_case.auth_use_case.ValidateConfirmPassword
import com.example.mobilecinema.domain.use_case.auth_use_case.ValidateEmail
import com.example.mobilecinema.domain.use_case.auth_use_case.ValidatePassword
import com.example.mobilecinema.presentation.RegisterModel
import com.example.mobilecinema.presentation.UiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class SignUpViewModel(
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateConfirmPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
    private val validateBirthDate: ValidateBirthDate = ValidateBirthDate(),
    private val addStorageUseCase: AddStorageUseCase,
    private val useCase: RegisterUseCase,
    private val converter: RegisterConverter
) : ViewModel() {
    private val _usersFlow =
        MutableStateFlow<UiState<AuthToken>>(UiState.Loading)

    val usersFlow: StateFlow<UiState<AuthToken>> = _usersFlow

    var state by mutableStateOf(RegisterModel())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _userName =
        MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    private val _userLogin =
        MutableStateFlow("")
    val userLogin: StateFlow<String> = _userLogin

    private val _userEmail =
        MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    private val _userPassword =
        MutableStateFlow("")
    val userPassword: StateFlow<String> = _userPassword

    private val _userConfirmPassword =
        MutableStateFlow("")
    val userConfirmPassword: StateFlow<String> = _userConfirmPassword

    private val _userBirthDate =
        MutableStateFlow<Instant?>(null)
    val userBirthDate: StateFlow<Instant?> = _userBirthDate

    private val _userSex =
        MutableStateFlow(0)
    val userSex = _userSex

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
                state = state.copy(birthDate = event.birthDate)

            }

            is RegistrationFormEvent.GenderChanged -> {
                state = state.copy(gender = event.gender)

            }

            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val confirmPasswordResult =
            validateConfirmPassword.execute(state.confirmPassword, state.confirmPassword)

        val hasError = listOf(
            emailResult,
            passwordResult,
            confirmPasswordResult
        ).any { it.errorManager != null }
        if (hasError) {
            state = state.copy(
                emailError = emailResult.errorManager,
                passwordError = passwordResult.errorManager,
                confirmPasswordError = confirmPasswordResult.errorManager
            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    fun load() {
        viewModelScope.launch {
            useCase.execute(
                RegisterUseCase.Request(
                    UserRegisterModel(
                        userName = userName.value,
                        userLogin = userLogin.value,
                        password = userConfirmPassword.value,
                        birthDate = userBirthDate.value!!,
                        gender = userSex.value
                    )
                )
            )
                .map {
                    converter.convert(it)
                }

                .collect {
                    _usersFlow.value = it

                }
            usersFlow.collect {
                when (it) {
                    is UiState.Loading -> {

                    }

                    is UiState.Error -> {
                        Log.d("login", it.errorMessage)
                    }

                    is UiState.Success -> {
                        Log.d("login", it.data.token)
                        addStorageUseCase.addStorage(it.data.token)
                    }
                }
            }
        }

    }

    fun addBirthDate(s: String?) {

    }

    fun setName(s: String) {
        _userName.value = s
    }

    fun setLogin(s: String) {
        _userLogin.value = s
    }

    fun setPassword(s: String) {
        _userConfirmPassword.value = s
    }

    fun setConfirmPassword(s: String) {
        _userConfirmPassword.value = s
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(s: String) {
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val localDateTime = LocalDate.parse(s, format)
        _userBirthDate.value =
            localDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant().toKotlinInstant()
    }

    fun setGender(value: Int) {
        _userSex.value = value
    }

    fun setEmail(s: String) {

    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }

}