package com.example.mobilecinema.presentation.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.SingInBinding
import com.example.mobilecinema.presentation.CinemaActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

class SingInFragment : Fragment(R.layout.sing_in) {
    private lateinit var binding: SingInBinding
    private lateinit var viewModel: SignInViewModel


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingInBinding.bind(view)
        binding.singInBackButton.setOnClickListener { close() }


        viewModel = ViewModelProvider(
            this, SignInViewModelFactory()
        )[SignInViewModel::class]

        val inButton = binding.singInInButton

        val loginText = binding.loginSingIn
        val passwordText = binding.passwordSingIn


        val deleteLogin = binding.loginCloseSingIn
        val watchPassword = binding.passwordWatchSingIn

        inButton.setOnClickListener {
            viewModel.load()
        }/*        val loadingScreen = binding.loadingScreen
                val bob1 = binding.bobLeft
                val bob2 = binding.bobRight*/
        lifecycleScope.launch {
/*            launch {
                viewModel.usersFlow.collect{
                    when(it){
                        is UiState.Loading->{
                            loadingScreen.visibility = View.VISIBLE
                            val animation = AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_bobin)
                            bob1.startAnimation(animation)
                            bob2.startAnimation(animation)
                        }
                        is UiState.Success->{
                            bob1.clearAnimation()
                            bob2.clearAnimation()
                            loadingScreen.visibility = View.GONE
                        }
                        is UiState.Error ->{
                            bob1.clearAnimation()
                            bob2.clearAnimation()
                            loadingScreen.visibility = View.GONE
                        }
                    }
                }
            }*/
            launch {
                viewModel.isConfirmed.collect {
                    if (it) startActivity(
                        Intent(
                            requireContext(), CinemaActivity::class.java
                        )
                    )
                    (requireContext() as Activity).finish()
                }
            }

            launch {
                viewModel.error.collect {
                    inButton.isEnabled = it == null
                }
            }

            launch {
                viewModel.userLogin.collect {
                    if (it == "") deleteLogin.visibility = View.INVISIBLE
                    else deleteLogin.visibility = View.VISIBLE
                }
            }

            launch {
                viewModel.userPassword.collect {
                    if (it == "") watchPassword.visibility = View.INVISIBLE
                    else watchPassword.visibility = View.VISIBLE
                }
            }

            launch {
                viewModel.error.collect {
                    if (viewModel.error.value != null && viewModel.error.value != "") {
                        Toasty.error(
                            requireContext(), viewModel.error.value!!, Toast.LENGTH_SHORT, true
                        ).show()

                    }
                }
            }
        }


        var show = false
        watchPassword.setOnClickListener {
            if (show) {
                watchPassword.isSelected = true
                passwordText.transformationMethod = PasswordTransformationMethod.getInstance()
                show = false
            } else {
                watchPassword.isSelected = false
                passwordText.transformationMethod = null
                show = true
            }
        }

        deleteLogin.setOnClickListener {
            viewModel.setLogin("")
            loginText.text = Editable.Factory.getInstance().newEditable("")
        }

        loginText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) loginText.addTextChangedListener(afterTextChanged = {
                viewModel.setLogin(it.toString())
            })
            else viewModel.checkErrors()
        }
        passwordText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) passwordText.addTextChangedListener(afterTextChanged = {
                viewModel.setPassword(it.toString())
            })
            else viewModel.checkErrors()
        }

        binding.rootSignInLayout.setOnTouchListener { _, _ ->
            viewModel.checkErrors()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            requireActivity().currentFocus?.clearFocus()
            true
        }
    }

    private fun close() {
        parentFragmentManager.popBackStack()
    }
}