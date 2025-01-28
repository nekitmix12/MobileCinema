package com.example.mobilecinema.presentation.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.ProfileBinding
import com.example.mobilecinema.presentation.start.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileScreen : Fragment(R.layout.profile) {
    private var binding: ProfileBinding? = null
    private var viewModel: ProfileViewModel? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileBinding.bind(view)

        setGradientContext(
            binding!!.informationProfile,
            ContextCompat.getColor(requireContext(), R.color.gradient_1),
            ContextCompat.getColor(requireContext(), R.color.gradient_2)
        )
        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory()
        )[ProfileViewModel::class]

        System.currentTimeMillis()
        binding?.timeProfile?.text = viewModel!!.getData()

        viewModel!!.loadProfile()

        lifecycleScope.launch {
            launch {
                viewModel!!.isLogout.collect {
                    if (it) {

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)

                    }
                }
            }
            viewModel!!.profile.collect {
                when (it) {
                    null -> {
                        binding!!.loadingScreen.visibility = View.VISIBLE
                    }

                    else -> {
                        Picasso.get().load(it.avatarLink)
                            .into(binding!!.imageProfileMain )
                        binding!!.loadingScreen.visibility = View.GONE
                        binding!!.nameProfile.text = it.name
                        binding!!.loginProfile.text =
                            Editable.Factory.getInstance().newEditable(it.nickName.toString())
                        binding!!.nameProfileEditText.text =
                            Editable.Factory.getInstance().newEditable(it.name)
                        binding!!.emailProfile.text =
                            Editable.Factory.getInstance().newEditable(it.email)
                        binding!!.birthDateProfile.text =
                            Editable.Factory.getInstance().newEditable(it.birthDate)

                        when (it.gender) {
                            0 -> binding!!.menButtonProfile.isSelected = true
                            1 -> binding!!.womenButtonProfile.isSelected = true
                        }
                    }
                }
            }
        }

        binding!!.logOutProfile.setOnClickListener {
            viewModel!!.logOut()
        }

        binding!!.loginProfile.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel?.profile?.value?.copy(name = binding!!.loginProfile.text.toString())
                    ?.let { viewModel!!.putProfile(profileDTO = it) }
            }
        }

        binding!!.nameProfileEditText.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {

                    val temp =
                        viewModel?.profile?.value?.copy(nickName = binding!!.nameProfileEditText.text.toString())
                    Log.d("temp", temp.toString())
                    temp?.let { viewModel!!.putProfile(profileDTO = it) }
                }
            }


        binding!!.emailProfile.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel?.profile?.value?.copy(email = binding!!.emailProfile.text.toString())
                    ?.let { viewModel!!.putProfile(profileDTO = it) }
            }
        }

        binding!!.birthDateProfile.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    viewModel?.profile?.value?.copy(birthDate = binding!!.birthDateProfile.text.toString())
                        ?.let { viewModel!!.putProfile(profileDTO = it) }
                }
            }

        val womenButtonSignUp = binding!!.womenButtonProfile
        val menButtonSignUp = binding!!.menButtonProfile

        menButtonSignUp.setOnClickListener {
            it.isSelected = true
            womenButtonSignUp.isSelected = false
            viewModel?.profile?.value?.copy(gender = 0)
                ?.let { viewModel!!.putProfile(profileDTO = it) }
        }

        womenButtonSignUp.setOnClickListener {
            it.isSelected = true
            menButtonSignUp.isSelected = false
            viewModel?.profile?.value?.copy(gender = 1)
                ?.let { viewModel!!.putProfile(profileDTO = it) }

        }


    }

    private fun setGradientContext(textView: TextView, vararg colors: Int) {
        val paint = textView.paint
        val width = paint.measureText(textView.text.toString())
        val shader =
            LinearGradient(0f, 0f, width, textView.textSize, colors, null, Shader.TileMode.CLAMP)

        textView.paint.setShader(shader)
        textView.setTextColor(colors[0])
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}