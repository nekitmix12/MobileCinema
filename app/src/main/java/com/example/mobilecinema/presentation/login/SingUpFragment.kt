package com.example.mobilecinema.presentation.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.R
import com.example.mobilecinema.data.UserRepositoryImpl
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.databinding.SingUpBinding
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase
import kotlinx.coroutines.Dispatchers

class SingUpFragment : Fragment(R.layout.sing_up) {
    private var binding: SingUpBinding? = null
    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingUpBinding.bind(view)
        binding?.singUpBackButton?.setOnClickListener { close() }
        val sharedPreferences = requireContext().getSharedPreferences(
            requireContext().getString(R.string.preference_is_logged_in), Context.MODE_PRIVATE
        )
        val tokenStorage = TokenStorageDataSourceImpl(sharedPreferences)
        val authInterceptor = AuthInterceptor(tokenStorage)
        val networkModule = NetworkModule()
        val apiServiceAuth = networkModule.provideAuthService(
            networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(authInterceptor)
            )
        )
        val authRemoteDataSource = AuthRemoteDataSourceImpl(apiServiceAuth)
        val userRepository = UserRepositoryImpl(authRemoteDataSource, tokenStorage)
        val configuration = UseCase.Configuration(Dispatchers.IO)
        val loginUserUseCase = LoginUserUseCase(configuration, userRepository)
        val authConverter = AuthConverter()
        viewModel = ViewModelProvider(
            this, LoginViewModelFactory(loginUserUseCase, authConverter)
        )[LoginViewModel::class]

        val nameSignUp = binding!!.nameSignUp
        val closeName = binding!!.nameCloseSignUp
        val loginSignUp = binding!!.loginSignUp
        val emailSignUp = binding!!.emailSignUp
        val passwordSignUp = binding!!.passwordSignUp
        val confirmPasswordSignUp = binding!!.confirmPasswordSignUp
        val womenButtonSignUp = binding!!.womenButtonSignUp
        val menButtonSignUp = binding!!.menButtonSignUp
        nameSignUp.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() == "") closeName.visibility = View.VISIBLE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setLogin(s.toString())
                if (s.toString() == "") closeName.visibility = View.INVISIBLE
            }

        })

    }

    private fun close() {
        parentFragmentManager.popBackStack()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}