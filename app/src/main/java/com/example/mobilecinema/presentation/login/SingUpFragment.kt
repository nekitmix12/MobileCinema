package com.example.mobilecinema.presentation.login

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import kotlinx.coroutines.Dispatchers

class SingUpFragment : Fragment(R.layout.sing_up) {
    private var binding: SingUpBinding? = null
    private lateinit var viewModel: SignUpViewModel

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
        val registerUserUseCase = RegisterUseCase(configuration, userRepository)
        val registerConverter = RegisterConverter()
        val addStorageUseCase = AddStorageUseCase(userRepository)

        viewModel = ViewModelProvider(
            this, SignUpViewModelFactory(registerUserUseCase, registerConverter, addStorageUseCase)
        )[SignUpViewModel::class]

        val nameSignUp = binding!!.nameSignUp
        val closeName = binding!!.nameCloseSignUp
        val loginSignUp = binding!!.loginSignUp
        val closeLogin = binding!!.loginCloseSignUp
        val emailSignUp = binding!!.emailSignUp
        val closeEmail = binding!!.emailCloseSignUp
        val passwordSignUp = binding!!.passwordSignUp
        val passwordWatch = binding!!.passwordWatchSignUp
        val confirmPasswordSignUp = binding!!.confirmPasswordSignUp
        val confirmPasswordWatch = binding!!.confirmPasswordWatchSignUp
        val dateSignUp = binding!!.dateSignUp
        val calendarDate = binding!!.calendarCloseSignUp
        val womenButtonSignUp = binding!!.womenButtonSignUp
        val menButtonSignUp = binding!!.menButtonSignUp
        nameSignUp.addTextChangedListener(object : TextWatcher {
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

        loginSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() == "") closeLogin.visibility = View.VISIBLE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setLogin(s.toString())
                if (s.toString() == "") closeLogin.visibility = View.INVISIBLE
            }

        })

        emailSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() == "") closeEmail.visibility = View.VISIBLE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setLogin(s.toString())
                if (s.toString() == "") closeEmail.visibility = View.INVISIBLE
            }

        })

        passwordSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() == "") passwordWatch.visibility = View.VISIBLE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setLogin(s.toString())
                if (s.toString() == "") passwordWatch.visibility = View.INVISIBLE
            }

        })

        confirmPasswordSignUp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (s.toString() == "") confirmPasswordWatch.visibility = View.VISIBLE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.setLogin(s.toString())
                if (s.toString() == "") confirmPasswordWatch.visibility = View.INVISIBLE
            }

        })

        dateSignUp.addTextChangedListener(object : TextWatcher {
            var isUpdate = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                viewModel.setLogin(s.toString())
                if (isUpdate) return

                isUpdate = true

                s?.let {
                    val text = StringBuffer()
                    val oText = s.toString().replace(".", "")
                    for (i in oText.indices)
                        if (i < 8) {
                            text.append(oText[i])
                            if(i == 1 && i != oText.length - 1)
                                 text.append(".")
                            if(i == 3 && i != oText.length - 1)
                                text.append(".")
                            }

                    dateSignUp.setText(text.toString())
                    dateSignUp.setSelection(text.length)
                    val pattern = "(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(19|20)\\d{2}".toRegex()
                    if(s.matches(pattern))
                        viewModel.addBirthDate(s.toString())
                    else
                        viewModel.addBirthDate(null)
                }
                isUpdate = false
            }

        })

        closeName.setOnClickListener {
            nameSignUp.text = Editable.Factory.getInstance().newEditable("")
        }

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        calendarDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                    dateSignUp.text = Editable.Factory.getInstance().newEditable(selectedDate)
                    viewModel.addBirthDate(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
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




