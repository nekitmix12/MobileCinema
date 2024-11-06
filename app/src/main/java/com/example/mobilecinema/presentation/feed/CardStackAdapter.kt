package com.example.mobilecinema.presentation.feed

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.R
import com.example.mobilecinema.data.model.movie.MovieElementModel
import com.example.mobilecinema.data.model.movie.MoviesPagedListModel
import com.squareup.picasso.Picasso


class CardStackAdapter(private val items: MoviesPagedListModel) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {


    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(items.movies!![position])
    }

    fun getItemMovies(position: Int): MovieElementModel {
        return items.movies!![position]
    }

    override fun getItemCount(): Int = items.movies!!.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val filmHolder: ImageView = view.findViewById(R.id.movie_card_id)
        val like: ImageView = itemView.findViewById(R.id.like)
        val dislike: ImageView = itemView.findViewById(R.id.dislike)


        fun bind(item: MovieElementModel) {
            filmHolder.setBackgroundColor(Color.TRANSPARENT)
            Picasso.get().load(item.poster)
                .placeholder(R.drawable.icon_background)
                .error(R.drawable.icon_background)
                .into(filmHolder)

        }
    }
}
