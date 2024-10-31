package com.example.mobilecinema.presentation.profile

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.ProfileBinding

class ProfileScreen:Fragment(R.layout.profile) {
    private var binding: ProfileBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileBinding.bind(view)
        setGradientContext(binding!!.informationProfile,
            ContextCompat.getColor(requireContext(),R.color.gradient_1),
            ContextCompat.getColor(requireContext(),R.color.gradient_2))
    }

    private fun setGradientContext(textView: TextView, vararg colors: Int ){
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        val shader = LinearGradient(0f,0f,width,textView.textSize,colors,null, Shader.TileMode.CLAMP)

        textView.paint.setShader(shader)
        textView.setTextColor(colors[0])
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}