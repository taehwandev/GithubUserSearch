@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.view.View
import android.view.animation.Animation

inline fun View.animationStart(
    animation: Animation,
    noinline onStart: (() -> Unit)? = null,
    noinline onEnd: (() -> Unit)? = null,
) {
    this.startAnimation(animation.also {
        it.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {
                // Do nothing.
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