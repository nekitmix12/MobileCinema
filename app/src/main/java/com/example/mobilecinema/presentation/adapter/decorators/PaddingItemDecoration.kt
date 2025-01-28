package com.example.mobilecinema.presentation.adapter.decorators

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PaddingItemDecoration(
    private val paddingTop: Int = 0,
    private val paddingBottom: Int = 0,
    private val paddingLeft: Int = 0,
    private val paddingRight: Int = 0,
    private vararg val viewTypes: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (!viewTypes.contains(parent.getChildViewHolder(view).itemViewType)) return

        with(outRect) {
            top = paddingTop.dpToPx(view.context)
            bottom = paddingBottom.dpToPx(view.context)
            left = paddingLeft.dpToPx(view.context)
            right = paddingRight.dpToPx(view.context)
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

}