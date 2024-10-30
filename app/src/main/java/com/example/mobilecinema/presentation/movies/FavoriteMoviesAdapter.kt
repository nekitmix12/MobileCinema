package com.example.mobilecinema.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.databinding.CommonFilmElementBinding
import com.squareup.picasso.Picasso

class FavoriteMoviesAdapter(val moviesElementModel: List<MovieElementModel>?):RecyclerView.Adapter<FavoriteMoviesAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = CommonFilmElementBinding.bind(view)
        fun bind(position:Int)= with(binding){
            val movie = moviesElementModel!![position].poster
            Picasso.get().load(movie)
                .placeholder(R.drawable.icon_background)
                .error(R.drawable.icon_background)
                .into(commonFilmElementImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.common_film_element, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = moviesElementModel?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }
}