package com.example.mobilecinema.presentation.sign_up

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.data.UserInfo
import com.example.mobilecinema.databinding.SingUpBinding
import com.example.mobilecinema.domain.use_case.UiState
import com.example.mobilecinema.presentation.CinemaActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

class SignUpFragment : Fragment(R.layout.sing_up) {
    private lateinit var binding: SingUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var registrationButton: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingUpBinding.bind(view)
        binding.singUpBackButton.setOnClickListener { close() }
        registrationButton = binding.regButton

        viewModel = ViewModelProvider(
            this, SignUpViewModelFactory()
        )[SignUpViewModel::class]


        val login = binding.loginSignUp
        val loginExit = binding.loginDeleteSignUp

        login.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                login.addTextChangedListener(afterTextChanged = { viewModel.setLogin(it.toString()) })
            } else {
                viewModel.checkErrors()
            }
        }

        loginExit.setOnClickListener {
            login.text = Editable.Factory.getInstance().newEditable("")
            viewModel.setLogin("")
        }

        val email = binding.emailSignUp
        val closeEmail = binding.emailCloseSignUp

        email.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                email.addTextChangedListener(afterTextChanged = { viewModel.setEmail(it.toString()) })
            } else {
                viewModel.checkErrors()
            }
        }

        closeEmail.setOnClickListener {
            email.text = Editable.Factory.getInstance().newEditable("")
            viewModel.setEmail("")
        }

        val name = binding.nameSignUp
        val closeName = binding.nameCloseSignUp

        name.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                name.addTextChangedListener(afterTextChanged = { viewModel.setName(it.toString()) })
            } else {
                viewModel.checkErrors()
                check()
            }
        }

        closeName.setOnClickListener {
            name.text = Editable.Factory.getInstance().newEditable("")
            viewModel.setName("")
        }

        val password = binding.passwordSignUp
        val passwordWatch = binding.passwordWatchSignUp

        password.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                password.addTextChangedListener(afterTextChanged = { viewModel.setPassword(it.toString()) })
            } else {
                viewModel.checkErrors()
                check()
            }
        }

        var isVisionPassword = false
        passwordWatch.setOnClickListener {
            if (isVisionPassword)

                password.transformationMethod = PasswordTransformationMethod.getInstance()
            else
                password.transformationMethod = null
            passwordWatch.isSelected = isVisionPassword
            isVisionPassword = !isVisionPassword

        }

        val confirmPassword = binding.confirmPasswordSignUp
        val confirmPasswordWatch = binding.confirmPasswordWatchSignUp

        confirmPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                confirmPassword.addTextChangedListener(afterTextChanged = {
                    viewModel.setConfirmPassword(
                        it.toString()
                    )
                })
            } else {
                viewModel.checkErrors()
                check()
            }
        }

        var isVisionConfirmPassword = false
        confirmPasswordWatch.setOnClickListener {
            if (isVisionConfirmPassword) {
                confirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                confirmPassword.transformationMethod = null
            }
            isVisionConfirmPassword = !isVisionConfirmPassword
            confirmPasswordWatch.isSelected = isVisionConfirmPassword
        }

        val date = binding.dateSignUp
        val calendarDate = binding.calendarCloseSignUp

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        calendarDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                    date.text = Editable.Factory.getInstance().newEditable(selectedDate)
                    viewModel.setDate(
                        selectedDay.toString(),
                        (selectedMonth + 1).toString(),
                        selectedYear.toString()
                    )
                    check()
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        val womenButton = binding.womenButtonSignUp
        val menButton = binding.menButtonSignUp

        womenButton.isSelected = true
        menButton.setOnClickListener {
            viewModel.setGender(1)
            menButton.isSelected = true
            womenButton.isSelected = false
            check()
        }

        womenButton.setOnClickListener {
            viewModel.setGender(0)
            menButton.isSelected = false
            womenButton.isSelected = true
            check()
        }


        lifecycleScope.launch {
            launch {
                viewModel.usersFlow.collect {
                    when (it) {
                        is UiState.Loading -> {
                        }

                        is UiState.Error -> {
                            Toasty.error(requireContext(), it.toString()).show()
                        }

                        is UiState.Success -> {
                            Toasty.success(requireContext(), "Success").show()
                            UserInfo.login = viewModel.userLogin.value
                            UserInfo.password = viewModel.userPassword.value
                            startActivity(
                                Intent(requireContext(), CinemaActivity::class.java)
                            )
                            (requireContext() as Activity).finish()
                        }
                    }
                }
            }
            launch {
                viewModel.error.collect {
                    check()
                    if (registrationButton.isEnabled)
                        viewModel.load()
                }
            }
            launch {
                viewModel.userLogin.collect {
                    Log.d("sign in", it)
                    if (it == "")
                        loginExit.visibility = View.INVISIBLE
                    else
                        loginExit.visibility = View.VISIBLE
                }
            }
            launch {
                viewModel.mail.collect {
                    if (it == "")
                        closeEmail.visibility = View.INVISIBLE
                    else
                        closeEmail.visibility = View.VISIBLE
                }
            }
            launch {
                viewModel.name.collect {
                    if (it == "")
                        closeName.visibility = View.INVISIBLE
                    else
                        closeName.visibility = View.VISIBLE
                }
            }
            launch {
                viewModel.userPassword.collect {
                    if (it == "")
                        passwordWatch.visibility = View.INVISIBLE
                    else
                        passwordWatch.visibility = View.VISIBLE

                }
            }
            launch {
                viewModel.confirmPassword.collect {
                    if (it == "")
                        confirmPasswordWatch.visibility = View.INVISIBLE
                    else
                        confirmPasswordWatch.visibility = View.VISIBLE
                    check()
                }
            }
        }


        binding.rootSignUpLayout.setOnTouchListener { _, _ ->
            check()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            requireActivity().currentFocus?.clearFocus()
            true
        }
    }


    private fun check() {
        if (viewModel.error.value == "" && viewModel.allNotEmpty) {
            registrationButton.isEnabled = true
        } else if (viewModel.error.value != "") {
            registrationButton.isEnabled = false
            Toasty.error(requireContext(), viewModel.error.value).show()
        }
    }

    private fun close() {
        parentFragmentManager.popBackStack()
    }

}