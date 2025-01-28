package com.example.mobilecinema.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.converters.auth.LogoutConverter
import com.example.mobilecinema.domain.converters.auth.PutProfileConverter
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase

class ProfileViewModelFactory(
    private val getProfileUseCase: GetProfileUseCase = GetProfileUseCase(),
    private val profileConverter: ProfileConverter = ProfileConverter(),
    private val logOutUseCase: LogOutUseCase = LogOutUseCase(),
    private val putProfileUseCase: PutProfileUseCase = PutProfileUseCase(),
    private val putProfileConverter: PutProfileConverter = PutProfileConverter(),
    private val logoutConverter: LogoutConverter = LogoutConverter(),
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                getProfileUseCase = getProfileUseCase,
                profileConverter = profileConverter,
                putProfileUseCase = putProfileUseCase,
                logOutUseCase = logOutUseCase,
                putProfileConverter = putProfileConverter,
                logoutConverter = logoutConverter
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}