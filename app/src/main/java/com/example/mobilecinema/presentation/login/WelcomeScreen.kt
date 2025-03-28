package com.example.mobilecinema.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.FragmentWelcomeBinding
import com.example.mobilecinema.presentation.sign_up.SignUpFragment

class WelcomeScreen: Fragment(R.layout.fragment_welcome) {

    private lateinit var binding : FragmentWelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

        binding.signInButton.setOnClickListener{navigateToSignIn()}
        binding.signUpButton.setOnClickListener{navigateToSignUp()}
    }


    private fun navigateToSignIn(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.welcomeFragmentContainerView, SingInFragment())
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToSignUp(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.welcomeFragmentContainerView, SignUpFragment())
            .addToBackStack(null)
            .commit()
    }


}