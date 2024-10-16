package com.example.mobilecinema.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.ActivityMainBinding

class CinemaActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        // Получаем NavHostFragment и NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding?.bottomNavigationView?.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_moviesScreen -> {
                    navController.navigate(R.id.moviesScreen)
                    true
                }
                R.id.navigation_feedScreen -> {
                    navController.navigate(R.id.feedScreen)
                    true
                }
                R.id.navigation_favoritesScreen -> {
                    navController.navigate(R.id.favoritesScreen)
                    true
                }
                R.id.navigation_profileScreen ->{
                    navController.navigate(R.id.profileScreen)
                    true
                }
                else -> false
            }
        }


        if (savedInstanceState == null) {
            navController.navigate(R.id.moviesScreen)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

