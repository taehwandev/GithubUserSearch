package tech.thdev.githubusersearch.base.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in ITEM : Any>(
    itemView: View,
) : RecyclerView.ViewHolder(
    itemView,
) {

    abstract fun onBind(item: ITEM)
}