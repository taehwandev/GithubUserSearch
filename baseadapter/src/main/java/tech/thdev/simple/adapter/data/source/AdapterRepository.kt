package tech.thdev.simple.adapter.data.source

import tech.thdev.simple.adapter.data.BaseAdapterItem

class AdapterRepository : BaseAdapterRepository<BaseAdapterItem.DefaultAdapterItem>() {

    override fun getItemViewType(position: Int): Int =
            list[position].viewType

    override fun addItems(viewType: Int, item: List<Any?>?) {
        item?.forEach {
            addItem(viewType, it)
        }
    }

    override fun getItem(position: Int): Any? =
            list[position].item

    override fun removeAt(position: Int) {
        list.removeAt(position)
    }

    override fun addItem(viewType: Int, item: Any?) {
        list.add(BaseAdapterItem.DefaultAdapterItem(viewType, item))
    }
}