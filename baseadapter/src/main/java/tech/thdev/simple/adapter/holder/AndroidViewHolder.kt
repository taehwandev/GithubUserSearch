package tech.thdev.simple.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

abstract class AndroidViewHolder(override val containerView: View)
    : RecyclerView.ViewHolder(containerView), LayoutContainer