package com.example.mobilecinema.presentation.sign_up

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper
import com.example.mobilecinema.data.UserInfo
import com.example.mobilecinema.data.model.auth.AuthToken
import com.example.mobilecinema.data.model.auth.UserRegisterModel
import com.example.mobilecinema.domain.model.RegisterModel
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import com.example.mobilecinema.domain.use_case.validate.ValidateBirthDate
import com.example.mobilecinema.domain.use_case.validate.ValidateConfirmPassword
import com.example.mobilecinema.domain.use_case.validate.ValidateEmail
import com.example.mobilecinema.domain.use_case.validate.ValidateGender
import com.example.mobilecinema.domain.use_case.validate.ValidateLogin
import com.example.mobilecinema.domain.use_case.validate.ValidateName
import com.example.mobilecinema.domain.use_case.validate.ValidatePassword
import com.example.mobilecinema.domain.converters.auth.RegisterConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.full.memberProperties

class SignUpViewModel(
    private val validateLogin: ValidateLogin = ValidateLogin(),
    private val validateName: ValidateName = ValidateName(),
    private val validateGender: ValidateGender = ValidateGender(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validatePassword: ValidatePassword = ValidatePassword(),
    private val validateConfirmPassword: ValidateConfirmPassword = ValidateConfirmPassword(),
    private val validateBirthDate: ValidateBirthDate = ValidateBirthDate(),
    private val stringHelper: StringHelper = StringHelper(),
    private val addStorageUseCase: AddStorageUseCase,
    private val registerUseCase: RegisterUseCase,
    private val registerConverter: RegisterConverter,

    ) : ViewModel() {
    private val _usersFlow =
        MutableStateFlow<UiState<AuthToken>>(UiState.Loading)

    val usersFlow: StateFlow<UiState<AuthToken>> = _usersFlow

    private val _userLogin =
        MutableStateFlow("")

    val userLogin: StateFlow<String> = _userLogin

    private var _mail = MutableStateFlow("")
    val mail: StateFlow<String> = _mail

    private var _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _userPassword =
        MutableStateFlow("")

    val userPassword: StateFlow<String> = _userPassword

    private var _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private var _date = MutableStateFlow("")
    val date: StateFlow<String> = _date

    private var _gender = MutableStateFlow(1)
    val gender: StateFlow<Int> = _gender

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    private var form = MutableStateFlow(RegisterModel())

    var allNotEmpty = false

    fun load() {
        if (_error.value != "") return
        val login = _userLogin.value
        val password = _userPassword.value
        viewModelScope.launch {
            registerUseCase.execute(
                RegisterUseCase.Request(
                    UserRegisterModel(
                        userLogin = userLogin.value,
                        userName = name.value,
                        email = mail.value,
                        password = userPassword.value,
                        birthDate = date.value,
                        gender = gender.value
                    )
                )
            )
                .map {
                    registerConverter.convert(it)
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
                    Log.d("token is: ", it.data.token)
                    UserInfo.login =login
                    UserInfo.password = password
                    addStorageUseCase.addStorage(it.data.token)
                }
            }
        }
    }

    fun setLogin(login: String) {
        _userLogin.value = login
        form.value = form.value.copy(loginError = validateLogin.execute(login).errorManager)
        checkErrors()
    }

    fun setEmail(email: String) {
        _mail.value = email
        form.value = form.value.copy(emailError = validateEmail.execute(email).errorManager)
        checkErrors()
    }

    fun setName(name: String) {
        _name.value = name
        form.value = form.value.copy(nameError = validateName.execute(name).errorManager)
        checkErrors()
    }

    fun setPassword(password: String) {
        _userPassword.value = password
        form.value =
            form.value.copy(passwordError = validatePassword.execute(password).errorManager)
        checkErrors()
    }

    fun setConfirmPassword(password: String) {
        _confirmPassword.value = password
        form.value = form.value.copy(
            confirmPasswordError = validateConfirmPassword.execute(
                password,
                confirmPassword.value
            ).errorManager
        )
        checkErrors()
    }

    fun setDate(day: String, month: String, year: String) {
        var data = ""
        data += "$year-"

        if (month.length == 1)
            data += 0
        data += "$month-"

        if (day.length == 1)
            data += "0"
        data += day


        _date.value = data
        form.value = form.value.copy(birthDateError = validateBirthDate.execute(data).errorManager)
        checkErrors()
    }

    fun setGender(gender: Int) {
        _gender.value = gender
        form.value = form.value.copy(genderError = validateGender.execute(gender).errorManager)
        checkErrors()
    }

    fun checkErrors() {
        val properties = RegisterModel::class.memberProperties
        var error = ""
        for (property in properties) {

            error += if (property.get(form.value) == null) "" else {
                if (error != "")
                    error += stringHelper.getString(R.string.and)
                property.get(form.value)
            }
        }
        _error.value = error
        checkEmpty()
    }

    private fun checkEmpty() {
        allNotEmpty = listOf(
            userLogin.value == "",
            mail.value == "",
            name.value == "",
            userPassword.value == "",
            confirmPassword.value == "",
            date.value == ""
        ).all { !it }
    }

}