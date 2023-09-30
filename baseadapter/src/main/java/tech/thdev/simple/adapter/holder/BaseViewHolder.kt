package tech.thdev.simple.adapter.holder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tech.thdev.simple.adapter.viewmodel.BaseAdapterViewModel

@Suppress("UNCHECKED_CAST")
abstract class BaseViewHolder<in ITEM : Any, VIEW_MODEL : BaseAdapterViewModel>(
    itemView: View,
) : RecyclerView.ViewHolder(
    itemView,
) {

    private lateinit var _viewModel: VIEW_MODEL

    var viewModel: VIEW_MODEL
        get() = _viewModel
        set(value) {
            _viewModel = value
            _viewModel.onInitViewModel()
        }

    fun checkItemAndBindViewHolder(item: Any?) {
        try {
            Log.i("TEMP", "checkItemAndBindViewHolder")
            onBindViewHolder(item as? ITEM)
        } catch (e: Exception) {
            e.printStackTrace()
            onBindViewHolder(null)
        }
    }

    /**
     * How to use.
     *
     * viewModel.onClick.... or the others use viewMode
     */
    abstract fun VIEW_MODEL.onInitViewModel()

    /**
     * Use viewHolder
     */
    abstract fun onBindViewHolder(item: ITEM?)
}