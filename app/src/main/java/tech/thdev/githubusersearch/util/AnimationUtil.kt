@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.support.v4.view.ViewCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator

inline fun View.animationStart(animation: Animation,
                               noinline onStart: (() -> Unit)? = null,
                               noinline onEnd: (() -> Unit)? = null) {
    this.startAnimation(animation.also {
        it.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                onEnd?.invoke()
            }

            override fun onAnimationStart(p0: Animation?) {
                onStart?.invoke()
            }
        })
    })
}

inline fun View.rotationAnimationStart(rotation: Float,
                                       duration: Long,
                                       overshootInterpolator: Float,
                                       noinline endAction: (() -> Unit)? = null) {
    ViewCompat.animate(this).rotation(rotation)
            .withLayer()
            .withEndAction {
                endAction?.invoke()
            }
            .setDuration(duration)
            .setInterpolator(OvershootInterpolator(overshootInterpolator)).start()
}