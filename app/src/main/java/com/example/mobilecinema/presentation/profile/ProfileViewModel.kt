package com.example.mobilecinema.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.model.ProfileModel
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.DataConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase
import com.example.mobilecinema.domain.use_case.validate.ValidateLink
import com.example.mobilecinema.domain.use_case.validate.ValidateBirthDate
import com.example.mobilecinema.domain.use_case.validate.ValidateEmail
import com.example.mobilecinema.domain.use_case.validate.ValidateGender
import com.example.mobilecinema.domain.use_case.validate.ValidateLogin
import com.example.mobilecinema.domain.use_case.validate.ValidateName
import com.example.mobilecinema.presentation.login.SignUpViewModel.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val validateLogin: ValidateLogin = ValidateLogin(),
    private val validateName: ValidateName = ValidateName(),
    private val validationLink: ValidateLink = ValidateLink(),
    private val validateGender: ValidateGender = ValidateGender(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validateBirthDate: ValidateBirthDate = ValidateBirthDate(),
    private val dataConverter: DataConverter = DataConverter(),
    private val getProfileUseCase: GetProfileUseCase,
    private val putProfileUseCase: PutProfileUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val profileConverter: ProfileConverter
) : ViewModel() {

    private var _profile = MutableStateFlow<UiState<ProfileDTO>>(UiState.Loading)
    private val profile: StateFlow<UiState<ProfileDTO>> = _profile

    var state by mutableStateOf(ProfileModel())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun loadProfile() {
        viewModelScope.launch {
            getProfileUseCase.execute()
                .map {
                    profileConverter.convert(it)
                }
                .collect {
                    _profile.value = it
                }
        }
    }

    fun putProfile(profileDTO: ProfileDTO) {
        viewModelScope.launch {
            putProfileUseCase.execute(
                PutProfileUseCase.Request(
                    profileDTO
                )
            )
        }
    }

    fun logOut() {
        viewModelScope.launch {
            logOutUseCase.execute()
        }
    }

    fun onEvent(event: ProfileFormEvent) {
        when (event) {
            is ProfileFormEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            is ProfileFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is ProfileFormEvent.LoginChanged -> {
                state = state.copy(login = event.login)

            }

            is ProfileFormEvent.BirthDateChanged -> {
                state = state.copy(
                    birthDate = if (event.birthDate.length > 10) dataConverter.convertDateFormat(
                        event.birthDate
                    ) else event.birthDate
                )

            }

            is ProfileFormEvent.GenderChanged -> {
                state = state.copy(gender = event.gender)

            }

            is ProfileFormEvent.Submit -> {
                submitData()
            }
        }
    }


    private fun submitData() {
        val emailResult = validateEmail.execute(state.email)
        val nameResult = validateName.execute(state.name)
        val loginResult = validateLogin.execute(state.login)
        val birthDateResult = validateBirthDate.execute(state.birthDate)
        val genderResult = validateGender.execute(state.gender)
        val avatarLinkResult = validationLink.execute(state.avatarLink)

        val hasError = listOf(
            nameResult,
            emailResult,
            loginResult,
            birthDateResult,
            genderResult,
            avatarLinkResult
        ).any { it.errorManager != null }

        if (hasError) {
            state = state.copy(
                nameError = nameResult.errorManager,
                emailError = emailResult.errorManager,
                loginError = loginResult.errorManager,
                birthDateError = birthDateResult.errorManager,
                genderError = genderResult.errorManager,
                avatarLink = avatarLinkResult.errorManager
            )

            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
        putProfile(
            ProfileDTO(
                name = state.name,
                nickName = state.login,
                email = state.email,
                birthDate = state.birthDate,
                gender = state.gender,
                id = state.id,
                avatarLink = state.avatarLink
            )
        )

    }
}