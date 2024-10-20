package com.example.mobilecinema.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecinema.R
import com.example.mobilecinema.data.TokenStorage
import com.example.mobilecinema.data.TokenStorageImpl
import com.example.mobilecinema.data.UserRepositoryImpl
import com.example.mobilecinema.data.datasource.remote.api_service.ApiServiceAuth
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSource
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.databinding.SingInBinding
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.repository.UserRepository
import com.example.mobilecinema.domain.use_case.user_use_case.LoginUserUseCase
import kotlinx.coroutines.Dispatchers

class SingInFragment:Fragment(R.layout.sing_in) {
    private var binding :SingInBinding? = null
    private lateinit var viewModel: LoginViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingInBinding.bind(view)
        binding?.singInBackButton?.setOnClickListener{close()}

        val sharedPreferences = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val tokenStorage = TokenStorageImpl(sharedPreferences)
        val authInterceptor = AuthInterceptor(tokenStorage)
        val networkModule = NetworkModule()
        val apiServiceAuth = networkModule.provideUserService(networkModule.provideRetrofit(networkModule.provideOkHttpClient(authInterceptor)))
        val authRemoteDataSource = AuthRemoteDataSourceImpl(apiServiceAuth)
        val userRepository = UserRepositoryImpl(authRemoteDataSource)
        val configuration = UseCase.Configuration(Dispatchers.IO)
        val loginUserUseCase =LoginUserUseCase(configuration,userRepository)
        val authConverter = AuthConverter()
        viewModel = ViewModelProvider(this,LoginViewModelFactory(loginUserUseCase,authConverter))[LoginViewModel::class]
        binding!!.singInInButton.setOnClickListener{viewModel.load()}
    }


    private fun close(){
        parentFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}