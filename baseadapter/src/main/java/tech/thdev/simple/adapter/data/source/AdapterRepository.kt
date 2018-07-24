package tech.thdev.simple.adapter.data.source

import tech.thdev.simple.adapter.data.BaseAdapterItem

class AdapterRepository : AbstractAdapterRepository<BaseAdapterItem.DefaultAdapterItem>() {

    override fun getItemViewType(position: Int): Int =
            position.getIndexOfBounds()?.let {
                list[it].viewType
            } ?: -1

    override fun addItems(viewType: Int, item: List<Any?>?) {
        item?.forEach {
            addItem(viewType, it)
        }
    }

    override fun getItem(position: Int): Any? =
            position.getIndexOfBounds()?.let {
                list[it].item
            }

    override fun removeAt(position: Int) {
        position.getIndexOfBounds()?.let {
            list.removeAt(it)
        }
    }

    override fun addItem(viewType: Int, item: Any?) {
        list.add(BaseAdapterItem.DefaultAdapterItem(viewType, item))
    }
}