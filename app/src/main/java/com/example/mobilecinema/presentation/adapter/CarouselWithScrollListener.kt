package com.example.mobilecinema.presentation.adapter

import android.animation.ObjectAnimator
import android.util.Log
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilecinema.databinding.HorizontalRecyclerViewWithCarouselBinding

class CarouselWithScrollListener(
    recyclerView: RecyclerView,
    private val progressBars: MutableList<ProgressBar>,
    private val binding: HorizontalRecyclerViewWithCarouselBinding,
) : CustomScrollListener(recyclerView, binding) {
    private val progressAnimators = mutableListOf<ObjectAnimator>()
    private var currentAnimatingPosition: Int = RecyclerView.NO_POSITION
    private var isUserScrolling = false
    private var isAutoScrolling = false
    private var lastScrollDirection: Int = 0

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                isUserScrolling = true
                cancelAllAnimations()
            }

            RecyclerView.SCROLL_STATE_IDLE -> {
                isUserScrolling = false
                if (!isPositionChanged()) {
                    resumeCurrentAnimation()
                } else {
                    updateProgressBars(recyclerView)
                }
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        lastScrollDirection = when {
            dx > 0 -> 1
            dx < 0 -> -1
            else -> 0
        }

        if (!isUserScrolling) {
            updateProgressBars(recyclerView)
        }
    }

    private fun updateProgressBars(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (currentAnimatingPosition == firstVisibleItemPosition) return

        currentAnimatingPosition = firstVisibleItemPosition

        if (progressAnimators.isEmpty()) {
            for (progressBar in progressBars) {
                progressAnimators.add(createAnimatorProgressBar(progressBar))
            }
        }

        for (i in progressBars.indices) {
            val progressBar = progressBars[i]
            val animator = progressAnimators[i]

            if (i == currentAnimatingPosition) {
                if (!animator.isRunning && !isUserScrolling) {
                    animator.start()
                }
            } else {
                animator.cancel()
                progressBar.progress = if (i < currentAnimatingPosition) 100 else 0
            }
        }

        Log.d("visible", "firstVisibleItemPosition: $firstVisibleItemPosition")
    }

    private fun createAnimatorProgressBar(progressBar: ProgressBar): ObjectAnimator =
        ObjectAnimator.ofInt(progressBar, "progress", 0, 100).apply {
            duration = 5000
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
            addListener(CustomAnimatorListener(onEnd = {
                if (!isUserScrolling && !isPositionChanged()) {
                    moveToNextItem()
                }
            }))
        }

    private fun moveToNextItem() {
        if (isUserScrolling || isAutoScrolling) return
        progressBars.forEach{
            it.clearAnimation()
        }
        isAutoScrolling = true
        with(binding.mainRecycleView) {
            val layoutManager = layoutManager as LinearLayoutManager
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val lastPosition = layoutManager.itemCount - 1

            if (currentPosition < lastPosition) {
                smoothScrollToPosition(currentPosition + 1)
            } else {
                scrollToPosition(0)
            }
        }
        isAutoScrolling = false
    }

    private fun cancelAllAnimations() {
        for (animator in progressAnimators) {
            animator.cancel()
        }
    }

    private fun isPositionChanged(): Boolean {
        val layoutManager =
            binding.mainRecycleView.layoutManager as? LinearLayoutManager ?: return false
        val currentPosition = layoutManager.findFirstVisibleItemPosition()
        return currentAnimatingPosition != currentPosition
    }

    private fun resumeCurrentAnimation() {
        if (currentAnimatingPosition != RecyclerView.NO_POSITION) {
            progressAnimators[currentAnimatingPosition].start()
        }
    }
}