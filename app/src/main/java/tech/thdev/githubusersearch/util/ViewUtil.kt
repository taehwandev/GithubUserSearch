@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

inline fun adapterScrollLinearLayoutManagerListener(
    crossinline onCallback: (visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) -> Unit
) = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.adapter?.itemCount ?: 0
        val firstVisibleItem = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
            ?: 0

        onCallback(visibleItemCount, totalItemCount, firstVisibleItem)
    }
}

inline fun Context.createErrorToast(
    duration: Int = Toast.LENGTH_SHORT,
    xOffset: Int = 0,
    yOffset: Int = 270, createView: () -> View
) =
    Toast(this).apply {
        setGravity(Gravity.TOP, xOffset, yOffset)
        this.duration = duration
        view = createView()
    }