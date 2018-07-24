package tech.thdev.simple.adapter.data.source

import tech.thdev.simple.adapter.data.BaseAdapterItem

abstract class AbstractAdapterRepository<T : BaseAdapterItem> : AdapterRepositoryInterface {

    protected fun Int.getIndexOfBounds() =
            this.takeIf { this in 0..itemCount }

    protected val list = mutableListOf<T>()

    override val itemCount: Int
        get() = list.size

    override fun addItems(viewType: Int, item: List<Any?>?) {
        item?.forEach {
            addItem(viewType, it)
        }
    }

    override fun removeAt(position: Int) {
        position.getIndexOfBounds()?.let {
            list.removeAt(it)
        }
    }

    override fun clear() {
        list.clear()
    }
}