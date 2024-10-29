package com.example.mobilecinema.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.squareup.picasso.Picasso

class TopImagesAdapter(private val moviesPagedListModel: MoviesPagedListModel): RecyclerView.Adapter<TopImagesAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imageView: ImageView  = view.findViewById(R.id.filmElView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.film_img_item, parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int= moviesPagedListModel.movies?.size ?: 0

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val movie = moviesPagedListModel.movies!![position].poster
        Picasso.get().load(movie)
            .placeholder(R.drawable.icon_background)
            .error(R.drawable.icon_background)
            .into(holder.imageView)
    }
}