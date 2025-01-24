package com.example.mobilecinema.presentation.start

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.di.MainContext
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.Console

class MainViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val profileConverter: ProfileConverter
):ViewModel() {
    private var _profile = MutableStateFlow<UiState<ProfileDTO>>(UiState.Loading)
    val profile: StateFlow<UiState<ProfileDTO>> = _profile

    private var _find = MutableStateFlow<Boolean?>(null)
    val find: StateFlow<Boolean?> = _find

    fun findToken() {
        viewModelScope.launch {
            _find.value = TokenStorageDataSourceImpl().getToken() != null
        }
    }

    fun checkToken(){
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