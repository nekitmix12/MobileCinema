package com.example.mobilecinema.presentation.profile

import androidx.lifecycle.ViewModel
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase
import com.example.mobilecinema.domain.use_case.validate.ValidateBirthDate
import com.example.mobilecinema.domain.use_case.validate.ValidateEmail
import com.example.mobilecinema.domain.use_case.validate.ValidateGender
import com.example.mobilecinema.domain.use_case.validate.ValidateLink
import com.example.mobilecinema.domain.use_case.validate.ValidateLogin
import com.example.mobilecinema.domain.use_case.validate.ValidateName

class ProfileViewModel(
    private val validateLogin: ValidateLogin = ValidateLogin(),
    private val validateName: ValidateName = ValidateName(),
    private val validationLink: ValidateLink = ValidateLink(),
    private val validateGender: ValidateGender = ValidateGender(),
    private val validateEmail: ValidateEmail = ValidateEmail(),
    private val validateBirthDate: ValidateBirthDate = ValidateBirthDate(),
    private val getProfileUseCase: GetProfileUseCase,
    private val putProfileUseCase: PutProfileUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val profileConverter: ProfileConverter,
) : ViewModel() {

    /*private var _profile = MutableStateFlow<UiState<ProfileDTO>>(UiState.Loading)
    val profile: StateFlow<UiState<ProfileDTO>> = _profile

    private val _profileUpdateStatus = MutableStateFlow<Result<PutProfileUseCase.Response>?>(null)
    val profileUpdateStatus: StateFlow<Result<PutProfileUseCase.Response>?> = _profileUpdateStatus

    var state by mutableStateOf(ProfileModel1())

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
            profile.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        state = state.copy(
                            id = it.data.id,
                            gender = it.data.gender,
                            email = it.data.email,
                            birthDate = it.data.birthDate,
                            login = it.data.nickName,
                            avatarLink = it.data.avatarLink,
                            name = it.data.name
                        )
                        Log.e(
                            "profileVM", state.copy(
                                id = it.data.id,
                                gender = it.data.gender,
                                email = it.data.email,
                                birthDate = it.data.birthDate,
                                login = it.data.nickName,
                                avatarLink = it.data.avatarLink,
                                name = it.data.name
                            ).toString()
                        )
                    }

                    is UiState.Error -> {}
                }
            }
        }*/
}

/*private fun putProfile(profileDTO: ProfileDTO) {
    viewModelScope.launch {
        putProfileUseCase.execute(
            PutProfileUseCase.Request(
                profileDTO
            )
        )
            .collect{
                _profileUpdateStatus.value = it

            }
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
            Log.d("asda", "sub")
            submitData()
            Log.d("asda", "sub")
        }
    }
}


private fun submitData() {
    Log.d("profileVm","startSub")
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
        //birthDateResult,
        genderResult,
        avatarLinkResult
    ).any {
        it.errorManager != null
    }
    Log.d("profileVm","is errors $hasError")

    if (hasError) {
        state = state.copy(
            nameError = nameResult.errorManager,
            emailError = emailResult.errorManager,
            loginError = loginResult.errorManager,
            birthDateError = birthDateResult.errorManager,
            genderError = genderResult.errorManager,
            avatarLinkError = avatarLinkResult.errorManager
        )
        Log.d("use_case", state.toString())
        return
    }

    viewModelScope.launch {
        validationEventChannel.send(ValidationEvent.Success)
    }
    Log.d(
        "profile", ProfileDTO(
            name = state.name,
            nickName = state.login,
            email = state.email,
            birthDate = state.birthDate,
            gender = state.gender,
            id = state.id,
            avatarLink = state.avatarLink
        ).toString()
    )
    Log.d("profileVm","start request")
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
}*/