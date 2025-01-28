package com.example.mobilecinema.presentation.adapter.delegates

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.HorizontalRecycleViewBinding
import com.example.mobilecinema.databinding.HorizontalRecyclerViewWithCarouselBinding
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.CustomScrollListener
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.Item
import com.example.mobilecinema.presentation.adapter.holders.HorizontalItemHolder
import com.example.mobilecinema.presentation.adapter.holders.HorizontalWithCarouselHolder
import com.example.mobilecinema.presentation.adapter.model.HorizontalItem
import com.example.mobilecinema.presentation.adapter.model.HorizontalWithCarouselModel

class HorizontalWithCarouselDelegate(
    private val delegatesList: List<Delegate<*, *>>,
    private val scrollListeners: List<CustomScrollListener>,
    private val outerDivider: Int,
) : Delegate<HorizontalRecyclerViewWithCarouselBinding, HorizontalWithCarouselModel> {
    override fun isRelativeItem(item: Item): Boolean = item is HorizontalWithCarouselModel

    override fun getLayoutId(): Int = R.layout.horizontal_recycler_view_with_carousel

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<HorizontalRecyclerViewWithCarouselBinding, HorizontalWithCarouselModel> {
        val binding = HorizontalRecyclerViewWithCarouselBinding.inflate(layoutInflater, parent, false)
        return HorizontalWithCarouselHolder(binding, delegatesList, outerDivider = outerDivider)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<HorizontalWithCarouselModel>() {
        override fun areItemsTheSame(oldItem: HorizontalWithCarouselModel, newItem: HorizontalWithCarouselModel) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: HorizontalWithCarouselModel, newItem: HorizontalWithCarouselModel) =
            oldItem == newItem

    }
}