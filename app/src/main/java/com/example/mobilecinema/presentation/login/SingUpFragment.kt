package com.example.mobilecinema.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.SingUpBinding

class SingUpFragment:Fragment(R.layout.sing_up) {
    private var binding : SingUpBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingUpBinding.bind(view)
        binding?.singUpBackButton?.setOnClickListener{close()}
    }

    private fun close(){
        parentFragmentManager.popBackStack()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}