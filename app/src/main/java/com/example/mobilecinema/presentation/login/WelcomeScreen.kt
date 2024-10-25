package com.example.mobilecinema.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.FragmentWelcomeBinding

class WelcomeScreen: Fragment(R.layout.fragment_welcome) {

    private var binding : FragmentWelcomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

        binding?.signInButton?.setOnClickListener{navigateToSignIn()}
        binding?.signUpButton?.setOnClickListener{navigateToSignUp()}
    }


    private fun navigateToSignIn(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.welcomeFragmentContainerView, SingInFragment())
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToSignUp(){
        parentFragmentManager.beginTransaction()
            .replace(R.id.welcomeFragmentContainerView, SingUpFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}