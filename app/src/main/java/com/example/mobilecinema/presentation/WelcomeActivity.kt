package com.example.mobilecinema.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.mobilecinema.R
import com.example.mobilecinema.presentation.login.WelcomeScreen

class WelcomeActivity: AppCompatActivity(R.layout.activity_welcome) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("welcome","+")
        if (savedInstanceState==null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.welcomeFragmentContainerView, WelcomeScreen())
                .commit()
        }
    }


}