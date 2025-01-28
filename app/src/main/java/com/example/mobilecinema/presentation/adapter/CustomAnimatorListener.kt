package com.example.mobilecinema.presentation.adapter

import android.animation.Animator
import android.util.Log

class CustomAnimatorListener(
    private val onStart: (Animator) -> Unit = {},
    private val onEnd: (Animator) -> Unit = {},
    private val onCansel: (Animator) -> Unit = {},
    private val onRepeat: (Animator) -> Unit = {},
) : Animator.AnimatorListener {
    override fun onAnimationStart(p0: Animator) {
        Log.d("animators", "animate onAnimationStart")

        onStart(p0)
    }

    override fun onAnimationEnd(p0: Animator) {
        Log.d("animators", "animate end")
        onEnd(p0)
    }

    override fun onAnimationCancel(p0: Animator) {
        Log.d("animators", "animate onAnimationCancel")
        onCansel(p0)
    }

    override fun onAnimationRepeat(p0: Animator) {
        Log.d("animators", "animate onAnimationRepeat")
        onRepeat(p0)
    }


}