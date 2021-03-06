@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

inline fun ImageView.loadUrl(url: String?,
                             @DrawableRes placeholder: Int,
                             crossinline onResourceReady: (() -> Boolean) = { false },
                             crossinline onLoadFail: (() -> Boolean) = { false }) {
    Glide.with(this.context)
            .load(url)
            .apply(RequestOptions.placeholderOf(placeholder))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean =
                        onLoadFail()


                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean =
                        onResourceReady()
            })
            .into(this)
}