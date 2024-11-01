package com.example.mobilecinema.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val profileConverter: ProfileConverter
):ViewModel() {
    private var _profile = MutableStateFlow<UiState<ProfileDTO>>(UiState.Loading)
    val profile: StateFlow<UiState<ProfileDTO>> = _profile

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
}