package com.example.mobilecinema.presentation.login

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobilecinema.R
import com.example.mobilecinema.data.repository.UserRepositoryImpl
import com.example.mobilecinema.data.datasource.local.TokenStorageDataSourceImpl
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import com.example.mobilecinema.data.network.AuthInterceptor
import com.example.mobilecinema.data.network.NetworkModule
import com.example.mobilecinema.databinding.SingUpBinding
import com.example.mobilecinema.domain.UseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.AddStorageUseCase
import com.example.mobilecinema.domain.use_case.auth_use_case.RegisterUseCase
import com.example.mobilecinema.presentation.CinemaActivity
import com.example.mobilecinema.domain.use_case.auth_use_case.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SingUpFragment : Fragment(R.layout.sing_up) {
    private var binding: SingUpBinding? = null
    private lateinit var viewModel: SignUpViewModel

    @SuppressLint(
        "UnsafeRepeatOnLifecycleDetector", "UseCompatLoadingForDrawables",
        "ClickableViewAccessibility"
    )
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
                viewModel.onEvent(RegistrationFormEvent.NameChanged(s.toString()))
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
                viewModel.onEvent(RegistrationFormEvent.LoginChanged(s.toString()))
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
                viewModel.onEvent(RegistrationFormEvent.EmailChanged(s.toString()))
                if (viewModel.state.emailError != null) {
                    viewModel.onEvent(RegistrationFormEvent.Submit)
                }
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
                viewModel.onEvent(RegistrationFormEvent.PasswordChanged(s.toString()))
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
                viewModel.onEvent(RegistrationFormEvent.ConfirmPasswordChanged(s.toString()))
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
                if (isUpdate) return
                viewModel.onEvent(RegistrationFormEvent.BirthDateChanged(s.toString()))
                isUpdate = true

                s?.let {
                    val text = StringBuffer()
                    val oText = s.toString().replace(".", "")
                    for (i in oText.indices)
                        if (i < 8) {
                            text.append(oText[i])
                            if (i == 1 && i != oText.length - 1)
                                text.append(".")
                            if (i == 3 && i != oText.length - 1)
                                text.append(".")
                        }

                    dateSignUp.setText(text.toString())
                    dateSignUp.setSelection(text.length)
                }
                isUpdate = false
            }

        })

        var clickFemale = false
        var clickMale = false
        menButtonSignUp.setOnClickListener {
            clickMale = !clickMale
            clickFemale = false
            womenButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)
            if (clickMale) {
                viewModel.onEvent(RegistrationFormEvent.GenderChanged(1))
                menButtonSignUp.setBackgroundResource(R.drawable.gradient_accent)
            } else {
                viewModel.onEvent(RegistrationFormEvent.GenderChanged(-1))
                menButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)
            }
        }

        womenButtonSignUp.setOnClickListener {
            clickMale = false
            clickFemale = !clickFemale
            menButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)
            if (clickFemale) {
                viewModel.onEvent(RegistrationFormEvent.GenderChanged(0))
                womenButtonSignUp.setBackgroundResource(R.drawable.gradient_accent)
            } else {
                viewModel.onEvent(RegistrationFormEvent.GenderChanged(-1))
                womenButtonSignUp.setBackgroundResource(R.drawable.sing_in_input_text)

            }
        }

        closeName.setOnClickListener {
            nameSignUp.text = Editable.Factory.getInstance().newEditable("")
        }

        closeLogin.setOnClickListener {
            loginSignUp.text = Editable.Factory.getInstance().newEditable("")
        }

        closeEmail.setOnClickListener {
            emailSignUp.text = Editable.Factory.getInstance().newEditable("")
        }

        var showPassword = true
        passwordWatch.setOnClickListener {
            if (showPassword) {
                passwordWatch.setImageDrawable(
                    requireContext().getDrawable(R.drawable.title_eye_off)
                )
                passwordSignUp.transformationMethod = PasswordTransformationMethod.getInstance()
                showPassword = false

            } else {
                passwordWatch.setImageDrawable(requireContext().getDrawable(R.drawable.title_eye))
                passwordSignUp.transformationMethod = null

                showPassword = true
            }
        }

        var showConfirmPassword = true
        confirmPasswordWatch.setOnClickListener {
            if (showConfirmPassword) {
                confirmPasswordWatch.setImageDrawable(
                    requireContext().getDrawable(R.drawable.title_eye_off)
                )
                confirmPasswordSignUp.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                showConfirmPassword = false

            } else {
                confirmPasswordWatch.setImageDrawable(requireContext().getDrawable(R.drawable.title_eye))
                confirmPasswordSignUp.transformationMethod =
                    null

                showConfirmPassword = true
            }
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
                    viewModel.onEvent(RegistrationFormEvent.BirthDateChanged(selectedDate))
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        binding!!.regButton.setOnClickListener {
            viewModel.onEvent(RegistrationFormEvent.Submit)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validationEvents.collectLatest { event ->
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
            viewModel.usersFlow.collect {
                when (it) {
                    is UiState.Loading -> {
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Success -> {
                        Toast.makeText(requireContext(), it.data.token, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(),CinemaActivity::class.java))
                    }
                }
            }
        }


        binding!!.rootSignUpLayout.setOnTouchListener { _, event ->
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