package tech.thdev.simple.adapter.data.source

import tech.thdev.simple.adapter.data.BaseAdapterItem

abstract class BaseAdapterRepository<T : BaseAdapterItem> : AdapterRepositoryInterface {

    protected val list = mutableListOf<T>()

    override val itemCount: Int
        get() = list.size

    override fun addItems(viewType: Int, item: List<Any?>?) {
        item?.forEach {
            addItem(viewType, it)
        }
    }

    override fun removeAt(position: Int) {
        list.removeAt(position)
    }
}