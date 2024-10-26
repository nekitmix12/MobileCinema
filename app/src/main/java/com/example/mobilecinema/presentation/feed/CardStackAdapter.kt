package com.example.mobilecinema.presentation.feed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.R
import com.squareup.picasso.Picasso


class CardStackAdapter(private val items: List<String>) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {


    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val filmHolder: ImageView = view.findViewById(R.id.movie_card_id)
        val like: ImageView = itemView.findViewById(R.id.like)
        val dislike: ImageView = itemView.findViewById(R.id.dislike)
        fun bind(item: String) {
            Picasso.get().load(item)
                .placeholder(R.drawable.icon_background)
                .error(R.drawable.icon_background)
                .into(filmHolder)
        }
    }
}
