package com.example.mobilecinema.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.databinding.MoviesElementAllFilmsBinding
import com.squareup.picasso.Picasso

class AllMoviesAdapter(val moviesMoviesElementModel: List<MovieElementModel>?,val ratings :List<Float>) :

    RecyclerView.Adapter<AllMoviesAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = MoviesElementAllFilmsBinding.bind(view)
        fun bind(position: Int) = with(binding){
//            rating.text = ratings[position].toString()
            val movie = moviesMoviesElementModel!![position].poster
            Picasso.get().load(movie)
                .placeholder(R.drawable.icon_background)
                .error(R.drawable.icon_background)
                .into(allFilmsElement)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movies_element_all_films, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int= moviesMoviesElementModel?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

}