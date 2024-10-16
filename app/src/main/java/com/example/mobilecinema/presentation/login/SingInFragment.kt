package com.example.mobilecinema.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.SingInBinding

class SingInFragment:Fragment(R.layout.sing_in) {
    private var binding :SingInBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SingInBinding.bind(view)
        binding?.singInBackButton?.setOnClickListener{close()}
    }

    private fun close(){
        parentFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}