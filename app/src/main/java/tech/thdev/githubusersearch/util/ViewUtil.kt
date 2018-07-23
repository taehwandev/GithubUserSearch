@file:Suppress("NOTHING_TO_INLINE")

package tech.thdev.githubusersearch.util

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

inline fun adapterScrollLinearLayoutManagerListener(
        crossinline onCallback: (visibleItemCount: Int, totalItemCount: Int, firstVisibleItem: Int) -> Unit) = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.adapter?.itemCount ?: 0
        val firstVisibleItem = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                ?: 0

        onCallback(visibleItemCount, totalItemCount, firstVisibleItem)
    }
}