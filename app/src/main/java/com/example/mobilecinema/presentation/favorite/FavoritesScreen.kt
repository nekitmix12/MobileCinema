package com.example.mobilecinema.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class FavoritesScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel =
            ViewModelProvider(this, FavoriteMoviesViewModelFactory())[FavoriteViewModel::class]

        lifecycleScope.launch {
            launch {
                viewModel.getFavoritesFilms()
            }
            launch {
                viewModel.getAllFavoriteGenres()
            }
            launch {
                viewModel.loadRatings()
            }
            launch {
                viewModel.isHaveAny()
            }
        }
         val navController = findNavController()
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                FavoriteScreen(
                    viewModel,
                    navController,
                    bottomNavigationView
                )
            }
        }
    }
}