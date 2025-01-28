package com.example.mobilecinema.presentation.adapter.holders


import android.content.Context
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.R
import com.example.mobilecinema.databinding.HorizontalRecyclerViewWithCarouselBinding
import com.example.mobilecinema.presentation.adapter.AdapterWithDelegates
import com.example.mobilecinema.presentation.adapter.BaseViewHolder
import com.example.mobilecinema.presentation.adapter.CarouselWithScrollListener
import com.example.mobilecinema.presentation.adapter.Delegate
import com.example.mobilecinema.presentation.adapter.model.HorizontalWithCarouselModel

class HorizontalWithCarouselHolder(
    binding: HorizontalRecyclerViewWithCarouselBinding,
    delegates: List<Delegate<*, *>>,
    outerDivider: Int,
) : BaseViewHolder<HorizontalRecyclerViewWithCarouselBinding, HorizontalWithCarouselModel>(binding) {
    private val delegateAdapter = AdapterWithDelegates(delegates)

    init {
        with(binding.mainRecycleView) {
            adapter = delegateAdapter
            layoutManager = LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
            )
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
    }


    override fun onBinding(item: HorizontalWithCarouselModel) {
        super.onBinding(item)
        val progressAnimators = mutableListOf<ProgressBar>()

        with(binding.linearLayout6) {
            removeAllViews()
            item.horizontalItems.forEachIndexed { index, _ ->
                val progressBar =
                    ProgressBar(context, null, es.dmoral.toasty.R.attr.progressBarStyle).apply {
                        id = index
                        layoutParams =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                                .apply {
                                    marginStart = dpToPx(context, 5)
                                    marginEnd = dpToPx(context, 5)
                                }
                        max = 100
                        progress = 0
                        progressDrawable =
                            ContextCompat.getDrawable(context, R.drawable.progress_bar)
                    }
                addView(progressBar)
                progressAnimators.add(progressBar)
            }
        }
        binding.mainRecycleView.addOnScrollListener(
            CarouselWithScrollListener(
                binding.mainRecycleView, progressAnimators, binding
            )
        )
        delegateAdapter.submitList(item.horizontalItems)

    }


    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
