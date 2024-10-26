package com.example.mobilecinema.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.mobilecinema.R

class MainActivity:AppCompatActivity() {

    private val sharedPref: SharedPreferences by lazy {
        baseContext.getSharedPreferences(this.getString(R.string.preference_is_logged_in),Context.MODE_PRIVATE)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (sharedPref.getString("authToken","")!="") {
            Intent(this, CinemaActivity::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }

        startActivity(intent)
        finish()
    }

    companion object{
        private const val PREF = "shared_prefs_name"
    }
}