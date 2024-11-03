package com.example.mobilecinema.presentation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobilecinema.R
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.UserRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.data.repository.AuthRepositoryImpl
import com.example.mobilecinema.data.repository.UserRepositoryImpl
import com.example.mobilecinema.databinding.ProfileBinding
import com.example.mobilecinema.domain.Result
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.converters.ProfileConverter
import com.example.mobilecinema.domain.repository.LocalStorageRepository
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.domain.use_case.auth_use_case.LogOutUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.GetProfileUseCase
import com.example.mobilecinema.domain.use_case.user_use_case.PutProfileUseCase
import com.example.mobilecinema.presentation.login.RegistrationFormEvent
import com.example.mobilecinema.presentation.login.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileScreen : Fragment(R.layout.profile) {
    private var binding: ProfileBinding? = null
    private var viewModel:ProfileViewModel?=null
    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileBinding.bind(view)

        setGradientContext(
            binding!!.informationProfile,
            ContextCompat.getColor(requireContext(), R.color.gradient_1),
            ContextCompat.getColor(requireContext(), R.color.gradient_2)
        )

        val sharedPreferences = requireContext().getSharedPreferences(
            requireContext().getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        )
        sharedPreferences.getString("authToken", "")?.let { Log.d("feed_screen", it) }
        val tokenStorage = TokenStorageDataSourceImpl(sharedPreferences)
        val networkModule = NetworkModule()
        val interceptor = AuthInterceptor(tokenStorage)
        val apiServiceUser = networkModule.provideUserService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val userRemoteDataSource = UserRemoteDataSourceImpl(apiServiceUser)
        val userRepository = UserRepositoryImpl(userRemoteDataSource)
        val configuration = UseCase.Configuration(Dispatchers.IO)
        val profileConverter = ProfileConverter()
        val apiServiceAuth = networkModule.provideAuthService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(interceptor)
            )
        )
        val authRemoteDataSourceImpl =
            AuthRemoteDataSourceImpl(apiServiceAuth)
        val favoriteUserRepository =
            AuthRepositoryImpl(authRemoteDataSourceImpl, tokenStorage)
        val logOutUseCase =
            LogOutUseCase(favoriteUserRepository, configuration)
        val getProfileUseCase = GetProfileUseCase(userRepository, configuration)
        val putProfileUseCase = PutProfileUseCase(userRepository,configuration)
        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(
                getProfileUseCase,
                profileConverter,
                logOutUseCase,
                putProfileUseCase
            )
        )[ProfileViewModel::class]


        viewModel!!.loadProfile()

        lifecycleScope.launch {
            viewModel!!.profile.collect{
                when(it){
                    is UiState.Loading->{}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        binding!!.nameProfile.text = it.data.name
                        binding!!.loginProfile.setText(it.data.nickName, TextView.BufferType.EDITABLE)
                        binding!!.nameProfileEditText.setText(it.data.name, TextView.BufferType.EDITABLE)
                        binding!!.emailProfile.setText(it.data.email,TextView.BufferType.EDITABLE)
                        binding!!.birthDateProfile.setText(it.data.birthDate, TextView.BufferType.EDITABLE)
                        when(it.data.gender){
                            0-> binding!!.menButtonProfile.setBackgroundResource(R.drawable.gradient_accent)
                            1->binding!!.womenButtonProfile.setBackgroundResource(R.drawable.gradient_accent)
                        }
                    }
                }
            }
        }

        binding!!.logOutProfile.setOnClickListener{
            viewModel!!.logOut()
        }

        binding!!.loginProfile.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
               viewModel!!.onEvent(ProfileFormEvent.LoginChanged(binding!!.loginProfile.text.toString()))

                viewModel!!.onEvent(ProfileFormEvent.Submit)
            }
        }

        binding!!.nameProfileEditText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel!!.onEvent(ProfileFormEvent.NameChanged(binding!!.nameProfileEditText.text.toString()))
                viewModel!!.onEvent(ProfileFormEvent.Submit)
            }
        }


        binding!!.emailProfile.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel!!.onEvent(ProfileFormEvent.EmailChanged(binding!!.emailProfile.text.toString()))
                viewModel!!.onEvent(ProfileFormEvent.Submit)
            }
        }

        binding!!.birthDateProfile.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel!!.onEvent(ProfileFormEvent.BirthDateChanged(binding!!.birthDateProfile.text.toString()))
                viewModel!!.onEvent(ProfileFormEvent.Submit)
            }
        }

        val womenButtonSignUp = binding!!.womenButtonProfile
        val menButtonSignUp = binding!!.menButtonProfile
        var clickFemale = false
        var clickMale = false
        menButtonSignUp.setOnClickListener {
            clickMale = !clickMale
            clickFemale = false
            womenButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)
            if (clickMale) {
                viewModel!!.onEvent(ProfileFormEvent.GenderChanged(1))
                menButtonSignUp.setBackgroundResource(R.drawable.gradient_accent)
            } else {
                viewModel!!.onEvent(ProfileFormEvent.GenderChanged(-1))
                menButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)
            }
        }

        womenButtonSignUp.setOnClickListener {
            clickMale = false
            clickFemale = !clickFemale
            menButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)
            if (clickFemale) {
                viewModel!!.onEvent(ProfileFormEvent.GenderChanged(0))
                womenButtonSignUp.setBackgroundResource(R.drawable.gradient_accent)
            } else {
                viewModel!!.onEvent(ProfileFormEvent.GenderChanged(-1))
                womenButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)

            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel!!.validationEvents.collectLatest { event ->
                    when (event) {
                        is SignUpViewModel.ValidationEvent.Success -> {
                            Toast.makeText(
                                requireContext(),
                                "Validation successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel!!.profileUpdateStatus.collect{
                    when(it){
                        is Result.Error ->
                        {
                            Log.e("profile","error is: ${it.exception}")
                        }
                        is Result.Success -> {
                            Log.e("profile","Success is: ${it.data}")

                        }
                        null -> {
                            Log.e("profile","null is: ${it}")
                        }
                    }
                }
            }
        }

    }

    private fun setGradientContext(textView: TextView, vararg colors: Int) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        val shader =
            LinearGradient(0f, 0f, width, textView.textSize, colors, null, Shader.TileMode.CLAMP)

        textView.paint.setShader(shader)
        textView.setTextColor(colors[0])
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}