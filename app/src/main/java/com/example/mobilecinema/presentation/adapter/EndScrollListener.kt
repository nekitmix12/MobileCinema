package com.example.mobilecinema.presentation.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EndScrollListener(private val onEnded:()->Unit):RecyclerView.OnScrollListener()  {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        if (lastItemPosition == layoutManager.itemCount - 1) {
            onEnded()
        }
    }
}