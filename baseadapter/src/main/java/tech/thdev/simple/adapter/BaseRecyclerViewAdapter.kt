package tech.thdev.simple.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import tech.thdev.simple.adapter.data.source.AdapterRepository
import tech.thdev.simple.adapter.data.source.AdapterRepositoryInterface
import tech.thdev.simple.adapter.holder.BaseViewHolder
import tech.thdev.simple.adapter.viewmodel.BaseAdapterViewModel

abstract class BaseRecyclerViewAdapter<VIEW_MODEL : BaseAdapterViewModel>(
        createViewModel: (adapterRepository: AdapterRepositoryInterface) -> VIEW_MODEL) :
        RecyclerView.Adapter<BaseViewHolder<*, VIEW_MODEL>>() {

    // Adapter data.
    private val adapterRepository: AdapterRepositoryInterface by lazy(LazyThreadSafetyMode.NONE) {
        AdapterRepository()
    }

    val viewModel: VIEW_MODEL = createViewModel(adapterRepository)

    init {
        viewModel.run {
            notifyDataSetChanged = this@BaseRecyclerViewAdapter::notifyDataSetChanged
            notifyItemChanged = this@BaseRecyclerViewAdapter::notifyItemChanged
            notifyItemRangeChanged = this@BaseRecyclerViewAdapter::notifyItemRangeChanged
            notifyItemInserted = this@BaseRecyclerViewAdapter::notifyItemInserted
            notifyItemRangeInserted = this@BaseRecyclerViewAdapter::notifyItemRangeInserted
            notifyItemRemoved = this@BaseRecyclerViewAdapter::notifyItemRemoved
            notifyItemRangeRemoved = this@BaseRecyclerViewAdapter::notifyItemRangeRemoved
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, VIEW_MODEL> =
            createViewHolder(viewType, parent).also { it.viewModel = viewModel }

    abstract fun createViewHolder(viewType: Int, parent: ViewGroup): BaseViewHolder<*, VIEW_MODEL>

    override fun getItemCount(): Int =
            adapterRepository.itemCount

    override fun getItemViewType(position: Int): Int =
            adapterRepository.getItemViewType(position)

    override fun onBindViewHolder(holder: BaseViewHolder<*, VIEW_MODEL>, position: Int) {
        holder.checkItemAndBindViewHolder(adapterRepository.getItem(position))
    }
}