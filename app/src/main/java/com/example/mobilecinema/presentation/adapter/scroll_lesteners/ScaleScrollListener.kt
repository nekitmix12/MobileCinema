package com.example.mobilecinema.presentation.adapter.scroll_lesteners

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScaleScrollListener : RecyclerView.OnScrollListener() {
    private var positionLastUpdated = 0
    private var isScale = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager

        val firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager?.findLastVisibleItemPosition()

        if (firstVisiblePosition != null && lastVisiblePosition != null) {
            for (position in firstVisiblePosition..lastVisiblePosition) {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)

                val itemView = viewHolder?.itemView

                if (itemView != null) {
                    if (itemView.left >= 0 && itemView.right <= recyclerView.width) {
                        if (position != positionLastUpdated) {
                            val item =
                                recyclerView.findViewHolderForAdapterPosition(positionLastUpdated)
                            item?.itemView?.scaleX = 1.0f
                            item?.itemView?.scaleY = 1.0f
                            positionLastUpdated = position
                            isScale = false
                        } else if (!isScale) {
                            val item = recyclerView.findViewHolderForAdapterPosition(position)
                            item?.itemView?.scaleX = 1.2f
                            item?.itemView?.scaleY = 1.2f
                            isScale = true
                        }
                        break
                    }
                }


            }
        }
    }
}