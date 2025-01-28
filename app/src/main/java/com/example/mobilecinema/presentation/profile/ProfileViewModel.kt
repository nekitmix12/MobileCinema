package com.example.mobilecinema.presentation.profile

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.StringHelper
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.model.auth.ProfileDTO
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.converters.auth.LogoutConverter
import com.example.mobilecinema.domain.converters.auth.PutProfileConverter
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase
import com.example.mobilecinema.domain.use_case.validate.ValidateBirthDate
import com.example.mobilecinema.domain.use_case.validate.ValidateEmail
import com.example.mobilecinema.domain.use_case.validate.ValidateGender
import com.example.mobilecinema.domain.use_case.validate.ValidateLink
import com.example.mobilecinema.domain.use_case.validate.ValidateLogin
import com.example.mobilecinema.domain.use_case.validate.ValidateName
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
    private val putProfileConverter: PutProfileConverter,
    private val logoutConverter: LogoutConverter,
) : ViewModel() {

    private var _profile = MutableStateFlow<ProfileDTO?>(null)
    val profile: StateFlow<ProfileDTO?> = _profile

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _isLogout = MutableStateFlow(false)
    val isLogout: StateFlow<Boolean> = _isLogout

    fun loadProfile() {
        viewModelScope.launch {
            getProfileUseCase.execute().map {
                profileConverter.convert(it)
            }.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        _profile.value = it.data
                    }

                    is UiState.Error -> {}
                }
            }
        }
    }

    fun urlToBitmap(urls: String?): Bitmap? {
        var result: Bitmap? = null

        Picasso.get().load(urls).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                result = bitmap
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })

        return result
    }

    fun putProfile(profileDTO: ProfileDTO) {
        viewModelScope.launch {
            putProfileUseCase.execute(
                PutProfileUseCase.Request(
                    profileDTO
                )
            ).map {
                putProfileConverter.convert(it)
            }.collect {
                if (it is UiState.Success) _error.value = null

                if (it is UiState.Error) _error.value = it.errorMessage

            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            logOutUseCase.execute().map { logoutConverter.convert(it) }.collect {
                if (it is UiState.Success) {
                    _error.value = null
                    _isLogout.value = true
                    TokenStorageDataSourceImpl().clearToken()
                }

                if (it is UiState.Error) _error.value = it.errorMessage
            }
        }
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


        _profile.value = _profile.value?.copy(birthDate = data)

    }

    fun isoToReadableDate(isoTime: String): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        val isoFormatWithZ = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        isoFormatWithZ.timeZone = TimeZone.getTimeZone("UTC")

        val readableFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
        val date: Date = try {
            isoFormat.parse(isoTime)!!
        } catch (e: Exception) {
            try {
                isoFormatWithZ.parse(isoTime)!!
            } catch (e: Exception) {
                Log.e("exception", e.message.toString())
                return "Некорректная дата"
            }
        }

        return date.let { readableFormat.format(it) } ?: "Некорректная дата"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData(): String {
        val currentMillis = System.currentTimeMillis()

        val zoneId = ZoneId.of("Asia/Novosibirsk")
        val zonedDateTime =
            ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(currentMillis), zoneId)
        val currentTime = zonedDateTime.toLocalTime()
        if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(
                LocalTime.of(
                    12,
                    0
                )
            )
        ) return StringHelper().getString(R.string.good_morning) + ","
        else if (currentTime.isBefore(
                LocalTime.of(
                    18,
                    0
                )
            )
        ) return StringHelper().getString(R.string.good_day) + ","
        else if (currentTime.isBefore(
                LocalTime.of(
                    24,
                    0
                )
            )
        ) return StringHelper().getString(R.string.good_evening) + ","
        else return StringHelper().getString(R.string.good_night) + ","
    }
}