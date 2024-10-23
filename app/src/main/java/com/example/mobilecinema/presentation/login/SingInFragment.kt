package com.example.mobilecinema.presentation.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.UserRepositoryImpl
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.databinding.SingInBinding
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.LoginUserUseCase
import com.example.mobilecinema.presentation.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingInFragment : Fragment(R.layout.sing_in) {
    private var binding: SingInBinding? = null
    private lateinit var viewModel: LoginViewModel

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingInBinding.bind(view)
        binding?.singInBackButton?.setOnClickListener { close() }

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

        binding!!.singInInButton.setOnClickListener {
            viewModel.load()
        }

        lifecycleScope.launch {
            viewModel.usersFlow.collect {
                when (it) {
                    is UiState.Loading -> {
                    }

                    is UiState.Error -> {
                    }

                    is UiState.Success -> {
                        Toast.makeText(requireContext(), it.data.token, Toast.LENGTH_SHORT)
                    }
                }
            }
        }
        val loginText = binding!!.loginSingIn
        val deleteLogin = binding!!.loginCloseSingIn
        val watchPassword = binding!!.passwordWatchSingIn
        val passwordText = binding!!.passwordSingIn
        var show = false
        watchPassword.setOnClickListener {
            if (show) {
                watchPassword.setImageDrawable(
                    requireContext().getDrawable(R.drawable.title_eye)
                )
                passwordText.transformationMethod = null
                show = false
            } else {
                watchPassword.setImageDrawable(requireContext().getDrawable(R.drawable.title_eye_off))
                passwordText.transformationMethod = PasswordTransformationMethod.getInstance()

                show = true
            }
        }

        deleteLogin.setOnClickListener {
            viewModel.setLogin("")
            loginText.text = Editable.Factory.getInstance().newEditable("")
        }

        loginText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() == "") deleteLogin.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setLogin(s.toString())
                if (s.toString() == "") deleteLogin.visibility = View.INVISIBLE

            }

        })
        passwordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("text",s.toString())
                if (s.toString() == "") watchPassword.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == "") watchPassword.visibility = View.INVISIBLE
                viewModel.setPassword(s.toString())
            }

        })
        binding!!.rootSignInLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }
    }

private fun hideKeyboard() {
    val activity = activity ?: return
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
    private fun close() {
        parentFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}