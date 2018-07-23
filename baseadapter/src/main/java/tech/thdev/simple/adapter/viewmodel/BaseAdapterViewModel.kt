package tech.thdev.simple.adapter.viewmodel

import tech.thdev.simple.adapter.data.source.AdapterRepositoryInterface

abstract class BaseAdapterViewModel(val adapterRepository: AdapterRepositoryInterface) {

    lateinit var notifyDataSetChanged: () -> Unit

    lateinit var notifyItemChanged: (position: Int) -> Unit

    lateinit var notifyItemRangeChanged: (position: Int, itemCount: Int) -> Unit

    lateinit var notifyItemInserted: (position: Int) -> Unit

    lateinit var notifyItemRangeInserted: (positionStart: Int, itemCount: Int) -> Unit

    lateinit var notifyItemRemoved: (position: Int) -> Unit

    lateinit var notifyItemRangeRemoved: (positionStart: Int, itemCount: Int) -> Unit
}